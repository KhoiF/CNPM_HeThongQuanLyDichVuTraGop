/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Bill;
import model.PaymentDetail;
import model.PaymentPeriod;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class BillDAO extends DAO {
    public BillDAO() {
        super();
    }

    public boolean addBill(Bill bill) {
        if (bill == null || bill.getPaymentDetails().isEmpty()) {
            return false;
        }
        if (bill.getPaymentDate() == null) {
            bill.setPaymentDate(LocalDate.now());
        }

        String billSql = "INSERT INTO tblBill (paymentDate, paymentType, note, userId) VALUES (?, ?, ?, ?)";
        String detailSql = "INSERT INTO tblPaymentDetail (allocatedAmount, note, paymentPeriodId, billId) VALUES (?, ?, ?, ?)";

        try {
            con.setAutoCommit(false);

            int billId;
            try (PreparedStatement psBill = con.prepareStatement(billSql, Statement.RETURN_GENERATED_KEYS)) {
                psBill.setDate(1, Date.valueOf(bill.getPaymentDate()));
                psBill.setString(2, bill.getPaymentType());
                psBill.setString(3, bill.getNote());
                psBill.setInt(4, bill.getUser().getId());
                psBill.executeUpdate();

                try (ResultSet rs = psBill.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("Không lấy được mã hóa đơn vừa tạo.");
                    }
                    billId = rs.getInt(1);
                    bill.setId(billId);
                }
            }

            try (PreparedStatement psDetail = con.prepareStatement(detailSql)) {
                for (PaymentDetail detail : bill.getPaymentDetails()) {
                    detail.setPaymentDate(bill.getPaymentDate());
                    psDetail.setDouble(1, detail.getAllocatedAmount());
                    psDetail.setString(2, detail.getNote());
                    psDetail.setString(3, detail.getPaymentPeriod().getId());
                    psDetail.setInt(4, billId);
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception rollbackError) {
                rollbackError.printStackTrace();
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public ArrayList<PaymentDetail> searchPaymentDetailInBill(PaymentPeriod period) {
        ArrayList<PaymentDetail> details = new ArrayList<>();
        if (period == null || period.getId() == null) {
            return details;
        }

        String sql = """
                SELECT pd.id, pd.allocatedAmount, pd.note, b.paymentDate
                FROM tblPaymentDetail pd
                JOIN tblBill b ON b.id = pd.billId
                WHERE pd.paymentPeriodId = ?
                ORDER BY b.paymentDate, pd.id
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, period.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date paymentDate = rs.getDate("paymentDate");
                    details.add(new PaymentDetail(
                            rs.getInt("id"),
                            rs.getDouble("allocatedAmount"),
                            rs.getString("note"),
                            period,
                            paymentDate == null ? null : paymentDate.toLocalDate()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }
}
