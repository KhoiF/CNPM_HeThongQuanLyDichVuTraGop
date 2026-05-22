import model.Bill;
import model.PaymentDetail;
import model.PaymentPeriod;

import java.time.LocalDate;

public class ModelSmokeTest {
    public static void main(String[] args) {
        PaymentPeriod period = new PaymentPeriod("PPX", 1, LocalDate.now().minusDays(5), 10_000_000);
        period.setPaidAmount(4_000_000);
        period.setOverdueInterest(30_000);
        period.setRemainingDebt(6_030_000);

        period.setPaidAmount(5_000_000);
        if (period.getOverdueInterest() != 30_000) {
            throw new AssertionError("Overdue interest should not be recalculated from aggregate paid amount.");
        }

        Bill bill = new Bill();
        PaymentDetail pendingDetail = bill.setPaymentDetail(period, 1_000_000, "pending");

        if (pendingDetail.getPaymentPeriod() != period) {
            throw new AssertionError("Pending detail period is wrong.");
        }
        if (bill.getPaymentAmount() != 1_000_000) {
            throw new AssertionError("Bill payment amount is wrong.");
        }
        if (period.getPaidAmountAfterPending() != 6_000_000) {
            throw new AssertionError("Paid amount after pending is wrong.");
        }
        if (period.getRemainingDebt() != 5_030_000) {
            throw new AssertionError("Remaining debt after pending is wrong.");
        }
        if (!"Ch\u01b0a thanh to\u00e1n".equals(period.getStatus())) {
            throw new AssertionError("Status is wrong.");
        }

        bill.setPaymentDetail(period, 6_030_000, "updated");

        if (bill.getPaymentDetails().size() != 1) {
            throw new AssertionError("Bill should update the existing period detail.");
        }
        if (bill.getPaymentAmount() != 6_030_000) {
            throw new AssertionError("Updated bill payment amount is wrong.");
        }
        if (!"\u0110\u00e3 thanh to\u00e1n".equals(period.getStatus())) {
            throw new AssertionError("Paid status is wrong.");
        }

        System.out.println("Model smoke test passed.");
    }
}
