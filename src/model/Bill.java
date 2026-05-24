/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class Bill implements Serializable {
    private int id;
    private LocalDate paymentDate;
    private String paymentType;
    private String note;
    private User user;
    private ArrayList<PaymentDetail> paymentDetails = new ArrayList<>();

    public Bill() {
    }

    public double getTotalPaymentAmount() {
        double total = 0;
        for (PaymentDetail detail : paymentDetails) {
            total += detail.getAllocatedAmount();
        }
        return total;
    }

    public double getPaymentAmount() {
        return getTotalPaymentAmount();
    }

    public PaymentDetail setPaymentDetail(PaymentPeriod period, double amount, String note) {
        PaymentDetail detail = findPaymentDetail(period);
        if (detail == null) {
            detail = new PaymentDetail();
            detail.setPaymentPeriod(period);
            paymentDetails.add(detail);
        }

        detail.setAllocatedAmount(amount);
        detail.setNote(note);
        detail.setPaymentDate(paymentDate);
        if (period != null) {
            period.setPendingPaymentAmount(amount);
        }
        return detail;
    }

    public void removePaymentDetail(PaymentPeriod period) {
        PaymentDetail detail = findPaymentDetail(period);
        if (detail != null) {
            paymentDetails.remove(detail);
        }
        if (period != null) {
            period.clearPendingPayment();
        }
    }

    private PaymentDetail findPaymentDetail(PaymentPeriod period) {
        if (period == null) {
            return null;
        }

        for (PaymentDetail detail : paymentDetails) {
            PaymentPeriod detailPeriod = detail.getPaymentPeriod();
            if (detailPeriod == period) {
                return detail;
            }
            if (detailPeriod != null && period.getId() != null && period.getId().equals(detailPeriod.getId())) {
                return detail;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(ArrayList<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails == null ? new ArrayList<>() : paymentDetails;
    }
}
