package model;

import java.io.Serializable;
import java.time.LocalDate;

public class PaymentDetail implements Serializable {
    private int id;
    private double allocatedAmount;
    private String note;
    private PaymentPeriod paymentPeriod;
    private LocalDate paymentDate;

    public PaymentDetail() {
    }

    public PaymentDetail(int id, double allocatedAmount, String note, PaymentPeriod paymentPeriod) {
        this(id, allocatedAmount, note, paymentPeriod, null);
    }

    public PaymentDetail(int id, double allocatedAmount, String note, PaymentPeriod paymentPeriod, LocalDate paymentDate) {
        this.id = id;
        this.allocatedAmount = allocatedAmount;
        this.note = note;
        this.paymentPeriod = paymentPeriod;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(double allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public PaymentPeriod getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(PaymentPeriod paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
