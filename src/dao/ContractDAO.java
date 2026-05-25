/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.BoughtItem;
import model.Client;
import model.Contract;
import model.Item;
import model.Partner;
import model.PaymentPeriod;
import model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class ContractDAO extends DAO {
    // Lai qua han tinh theo ngay: 0.001 = 0.1%/ngay.
    private static final double OVERDUE_INTEREST_RATE = 0.001;
    private static final String PAID_STATUS = "\u0110\u00e3 thanh to\u00e1n";
    private static final String UNPAID_STATUS = "Ch\u01b0a thanh to\u00e1n";

    public ContractDAO() {
        super();
    }

    public Contract searchContract(String contractId) {
        String sql = """
                SELECT c.id AS contractId, c.signDate, c.loanTerm,
                       cl.id AS clientId, cl.idCard, cl.fullName AS clientName, cl.tel, cl.address AS clientAddress, cl.email AS clientEmail,
                       u.id AS userId, u.fullName AS userName, u.username, u.password, u.position,
                       u.tel AS userTel, u.email AS userEmail, u.address AS userAddress,
                       p.id AS partnerId, p.partnerName, p.phoneNumber, p.address AS partnerAddress, p.email AS partnerEmail,
                       p.bankName, p.accountNumber, p.description AS partnerDescription
                FROM tblContract c
                JOIN tblClient cl ON c.clientId = cl.id
                JOIN tblUser u ON c.userId = u.id
                JOIN tblPartner p ON c.partnerId = p.id
                WHERE c.id = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, contractId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Contract contract = new Contract();
                contract.setId(rs.getString("contractId"));
                contract.setSignDate(rs.getDate("signDate").toLocalDate());
                contract.setLoanTerm(rs.getInt("loanTerm"));
                contract.setClient(new Client(
                        rs.getInt("clientId"),
                        rs.getString("idCard"),
                        rs.getString("clientName"),
                        rs.getString("tel"),
                        rs.getString("clientAddress"),
                        rs.getString("clientEmail")
                ));
                contract.setUser(new User(
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("position"),
                        getOptionalString(rs, "userTel"),
                        getOptionalString(rs, "userEmail"),
                        getOptionalString(rs, "userAddress")
                ));
                contract.setPartner(new Partner(
                        rs.getInt("partnerId"),
                        rs.getString("partnerName"),
                        rs.getString("phoneNumber"),
                        rs.getString("partnerAddress"),
                        rs.getString("partnerEmail"),
                        rs.getString("bankName"),
                        rs.getString("accountNumber"),
                        rs.getString("partnerDescription")
                ));

                loadBoughtItems(contract);
                loadPaymentPeriods(contract);
                return contract;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadBoughtItems(Contract contract) throws Exception {
        String sql = """
                SELECT bi.id AS boughtItemId, bi.price AS boughtPrice, bi.quantity, bi.sellOff, bi.note,
                       i.id AS itemId, i.name, i.price AS itemPrice, i.unit
                FROM tblBoughtItem bi
                JOIN tblItem i ON bi.itemId = i.id
                WHERE bi.contractId = ?
                ORDER BY bi.id
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, contract.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item(
                            rs.getString("itemId"),
                            rs.getString("name"),
                            rs.getDouble("itemPrice"),
                            rs.getString("unit")
                    );
                    contract.getBoughtItems().add(new BoughtItem(
                            rs.getString("boughtItemId"),
                            rs.getDouble("boughtPrice"),
                            rs.getInt("quantity"),
                            rs.getDouble("sellOff"),
                            rs.getString("note"),
                            item
                    ));
                }
            }
        }
    }

    private void loadPaymentPeriods(Contract contract) throws Exception {
        // Chi lay thong tin dot thanh toan. Lich su tra tien duoc load rieng
        String sql = """
                SELECT pp.id AS periodId, pp.period, pp.dueDate
                FROM tblPaymentPeriod pp
                WHERE pp.contractId = ?
                ORDER BY pp.period
                """;

        List<PaymentPeriod> periods = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, contract.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentPeriod period = new PaymentPeriod(
                            rs.getString("periodId"),
                            rs.getInt("period"),
                            rs.getDate("dueDate").toLocalDate(),
                            0
                    );
                    periods.add(period);
                }
            }
        }

        // Lich su thanh toan chi duoc dung tam trong DAO de tinh cac so tong hop.
        Map<String, List<PaymentHistory>> historyByPeriod = loadPaymentHistory(contract.getId());
        double derivedPayableAmount = calculatePayableAmount(contract, periods.size());
        LocalDate today = LocalDate.now();
        for (PaymentPeriod period : periods) {
            period.setTotalAmountPayable(derivedPayableAmount);
            PeriodBalance balance = calculatePeriodBalance(
                    derivedPayableAmount,
                    period.getDueDate(),
                    historyByPeriod.get(period.getId()),
                    today
            );
            period.setPaidAmount(balance.paidAmount);
            period.setOverdueInterest(balance.overdueInterest);
            period.setRemainingDebt(balance.remainingDebt);
            period.setStatus(balance.remainingDebt <= 0 ? PAID_STATUS : UNPAID_STATUS);
            contract.getPaymentPeriods().add(period);
        }
    }

    private Map<String, List<PaymentHistory>> loadPaymentHistory(String contractId) throws Exception {
        // Lay so tien va ngay thanh toan cua tung payment detail, roi gom theo ma dot.
        String sql = """
                SELECT pd.id, pd.paymentPeriodId, pd.allocatedAmount, b.paymentDate
                FROM tblPaymentDetail pd
                JOIN tblBill b ON b.id = pd.billId
                JOIN tblPaymentPeriod pp ON pp.id = pd.paymentPeriodId
                WHERE pp.contractId = ?
                ORDER BY pd.paymentPeriodId, b.paymentDate, pd.id
                """;

        Map<String, List<PaymentHistory>> historyByPeriod = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, contractId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date paymentDate = rs.getDate("paymentDate");
                    String paymentPeriodId = rs.getString("paymentPeriodId");
                    PaymentHistory history = new PaymentHistory(
                            rs.getInt("id"),
                            rs.getDouble("allocatedAmount"),
                            paymentDate == null ? null : paymentDate.toLocalDate()
                    );
                    historyByPeriod.computeIfAbsent(paymentPeriodId, key -> new ArrayList<>()).add(history);
                }
            }
        }
        return historyByPeriod;
    }

    private PeriodBalance calculatePeriodBalance(
            double payableAmount,
            LocalDate dueDate,
            List<PaymentHistory> paymentHistory,
            LocalDate asOfDate
    ) {
        // principalRemaining: tien goc con lai cua dot.
        // overdueInterest: tien lai qua han tinh tren tien goc con lai.
        double principalRemaining = Math.max(0, payableAmount);
        double overdueInterest = 0;
        double totalPaid = 0;

        // DB khong luu dueDate tai thoi diem thanh toan, nen tien da tra duoc tru vao goc truoc.
        // Cach nay giu dung du no goc con lai khi dueDate bi sua truc tiep trong MySQL.
        List<PaymentHistory> histories = paymentHistory == null ? new ArrayList<>() : new ArrayList<>(paymentHistory);
        histories.sort(ContractDAO::comparePaymentHistory);

        for (PaymentHistory history : histories) {
            double paymentAmount = Math.max(0, history.allocatedAmount);
            if (paymentAmount <= 0) {
                continue;
            }

            LocalDate paymentDate = history.paymentDate == null ? asOfDate : history.paymentDate;
            if (paymentDate == null || (asOfDate != null && paymentDate.isAfter(asOfDate))) {
                continue;
            }

            double principalPayment = Math.min(principalRemaining, paymentAmount);
            principalRemaining -= principalPayment;
            totalPaid += paymentAmount;
        }

        // Neu qua han va van con goc, tinh lai tren phan goc con lai theo dueDate hien tai trong DB.
        if (dueDate != null && asOfDate != null && asOfDate.isAfter(dueDate) && principalRemaining > 0) {
            long lateDays = ChronoUnit.DAYS.between(dueDate, asOfDate);
            overdueInterest = principalRemaining * OVERDUE_INTEREST_RATE * lateDays;
        }

        return new PeriodBalance(
                totalPaid,
                Math.max(0, overdueInterest),
                Math.max(0, principalRemaining + overdueInterest)
        );
    }

    private static int comparePaymentHistory(PaymentHistory left, PaymentHistory right) {
        LocalDate leftDate = left.paymentDate == null ? LocalDate.MAX : left.paymentDate;
        LocalDate rightDate = right.paymentDate == null ? LocalDate.MAX : right.paymentDate;
        int dateCompare = leftDate.compareTo(rightDate);
        if (dateCompare != 0) {
            return dateCompare;
        }
        return Integer.compare(left.id, right.id);
    }

    private static boolean isAfterDueDate(LocalDate dueDate, LocalDate paymentDate) {
        return dueDate != null && paymentDate != null && paymentDate.isAfter(dueDate);
    }

    private double calculatePayableAmount(Contract contract, int periodCount) {
        int divisor = periodCount > 0 ? periodCount : contract.getLoanTerm();
        if (divisor <= 0) {
            return 0;
        }
        return contract.getTotalAmount() / divisor;
    }

    // DTO noi bo cua DAO
    private static class PaymentHistory {
        private final int id;
        private final double allocatedAmount;
        private final LocalDate paymentDate;            

        private PaymentHistory(int id, double allocatedAmount, LocalDate paymentDate) {
            this.id = id;
            this.allocatedAmount = allocatedAmount;
            this.paymentDate = paymentDate;
        }
    }

    // Ket qua tinh toan cuoi cung duoc set vao PaymentPeriod de hien thi.
    private static class PeriodBalance {
        private final double paidAmount;
        private final double overdueInterest;
        private final double remainingDebt;

        private PeriodBalance(double paidAmount, double overdueInterest, double remainingDebt) {
            this.paidAmount = paidAmount;
            this.overdueInterest = overdueInterest;
            this.remainingDebt = remainingDebt;
        }
    }
}
