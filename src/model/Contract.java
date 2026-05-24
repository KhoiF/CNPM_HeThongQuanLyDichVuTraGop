/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class Contract implements Serializable {
    private String id;
    private LocalDate signDate;
    private int loanTerm;
    private Client client;
    private User user;
    private Partner partner;
    private final ArrayList<BoughtItem> boughtItems = new ArrayList<>();
    private final ArrayList<PaymentPeriod> paymentPeriods = new ArrayList<>();

    public Contract() {
    }

    public double getTotalAmount() {
        double total = 0;
        for (BoughtItem boughtItem : boughtItems) {
            total += boughtItem.getLineTotal();
        }
        return total;
    }

    public double getTotalRemainingDebt() {
        double total = 0;
        for (PaymentPeriod period : paymentPeriods) {
            total += period.getRemainingDebt();
        }
        return total;
    }

    public List<PaymentPeriod> getPendingPeriods() {
        ArrayList<PaymentPeriod> result = new ArrayList<>();
        for (PaymentPeriod period : paymentPeriods) {
            if (period.hasPendingPayment()) {
                result.add(period);
            }
        }
        return result;
    }

    public void clearPendingPayments() {
        for (PaymentPeriod period : paymentPeriods) {
            period.clearPendingPayment();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public ArrayList<BoughtItem> getBoughtItems() {
        return boughtItems;
    }

    public ArrayList<PaymentPeriod> getPaymentPeriods() {
        return paymentPeriods;
    }
}
