/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Admin
 */
public class PaymentPeriod implements Serializable {
    private static final String PAID_STATUS = "\u0110\u00e3 thanh to\u00e1n";
    private static final String UNPAID_STATUS = "Ch\u01b0a thanh to\u00e1n";

    private String id;
    private int period;
    private LocalDate dueDate;
    private double payableAmount;
    private double paidAmount;
    private double remainingDebt;
    private double overdueInterest;
    private String status;
    private double pendingPaymentAmount;

    public PaymentPeriod() {
    }

    public PaymentPeriod(String id, int period, LocalDate dueDate, double payableAmount) {
        this.id = id;
        this.period = period;
        this.dueDate = dueDate;
        this.payableAmount = payableAmount;
        this.remainingDebt = Math.max(0, payableAmount);
        updateStatus();
    }

    public void recalculate() {
        updateStatus();
    }

    public double getRemainingDebt() {
        return Math.max(0, remainingDebt - pendingPaymentAmount);
    }

    public double getPaidAmountAfterPending() {
        return paidAmount + pendingPaymentAmount;
    }

    public String getStatus() {
        if (getRemainingDebt() <= 0) {
            return PAID_STATUS;
        }
        return status == null ? UNPAID_STATUS : status;
    }

    public boolean hasPendingPayment() {
        return pendingPaymentAmount > 0;
    }

    public void clearPendingPayment() {
        pendingPaymentAmount = 0;
    }

    private void updateStatus() {
        status = remainingDebt <= 0 ? PAID_STATUS : UNPAID_STATUS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        setTotalAmountPayable(payableAmount);
    }

    public double getTotalAmountPayable() {
        return payableAmount;
    }

    public void setTotalAmountPayable(double totalAmountPayable) {
        this.payableAmount = totalAmountPayable;
        if (paidAmount <= 0 && overdueInterest <= 0) {
            this.remainingDebt = Math.max(0, totalAmountPayable);
            updateStatus();
        }
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = Math.max(0, paidAmount);
    }

    public void setRemainingDebt(double remainingDebt) {
        this.remainingDebt = Math.max(0, remainingDebt);
        updateStatus();
    }

    public double getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(double overdueInterest) {
        this.overdueInterest = Math.max(0, overdueInterest);
    }

    public double getPendingPaymentAmount() {
        return pendingPaymentAmount;
    }

    public void setPendingPaymentAmount(double pendingPaymentAmount) {
        this.pendingPaymentAmount = Math.max(0, pendingPaymentAmount);
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
