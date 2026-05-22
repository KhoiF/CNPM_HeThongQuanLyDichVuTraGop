package model;

import java.io.Serializable;

public class Partner implements Serializable {
    private int id;
    private String partnerName;
    private String phoneNumber;
    private String address;
    private String email;
    private String bankName;
    private String accountNumber;
    private String description;

    public Partner() {
    }

    public Partner(int id, String partnerName, String phoneNumber, String address, String email,
                   String bankName, String accountNumber, String description) {
        this.id = id;
        this.partnerName = partnerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
