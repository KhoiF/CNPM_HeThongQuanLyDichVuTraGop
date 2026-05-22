import dao.BillDAO;
import dao.ContractDAO;
import dao.UserDAO;
import model.Contract;
import model.PaymentDetail;
import model.PaymentPeriod;
import model.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class DaoSmokeTest {
    public static void main(String[] args) {
        User user = new User();
        user.setUsername("quan");
        user.setPassword("123456");

        if (!new UserDAO().checkLogin(user)) {
            throw new AssertionError("Login smoke test failed. Check MySQL and imported SQL data.");
        }

        Contract contract = new ContractDAO().searchContract("HD01");
        if (contract == null || contract.getBoughtItems().isEmpty() || contract.getPaymentPeriods().isEmpty()) {
            throw new AssertionError("Contract smoke test failed.");
        }

        PaymentPeriod firstPeriod = contract.getPaymentPeriods().get(0);
        double expectedPayableAmount = contract.getTotalAmount() / contract.getPaymentPeriods().size();
        if (Math.abs(firstPeriod.getPayableAmount() - expectedPayableAmount) > 0.001) {
            throw new AssertionError("Derived payment period payable amount is wrong.");
        }

        ArrayList<PaymentDetail> details = new BillDAO().searchPaymentDetailInBill(firstPeriod);
        if (details.isEmpty()) {
            throw new AssertionError("Payment detail history smoke test failed.");
        }
        if (details.get(0).getPaymentDate() == null) {
            throw new AssertionError("Payment detail payment date was not loaded from bill.");
        }

        Contract partialContract = new ContractDAO().searchContract("HD02");
        PaymentPeriod partialPeriod = partialContract.getPaymentPeriods().get(0);
        double expectedPrincipal = partialPeriod.getPayableAmount() - 1_000_000;
        long lateDays = Math.max(0, ChronoUnit.DAYS.between(partialPeriod.getDueDate(), LocalDate.now()));
        double expectedOverdueInterest = expectedPrincipal * 0.001 * lateDays;
        if (Math.abs(partialPeriod.getOverdueInterest() - expectedOverdueInterest) > 0.001) {
            throw new AssertionError("Overdue interest from payment history is wrong.");
        }

        System.out.println("DAO smoke test passed.");
    }
}
