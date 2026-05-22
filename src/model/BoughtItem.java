package model;

import java.io.Serializable;

public class BoughtItem implements Serializable {
    private String id;
    private double price;
    private int quantity;
    private double sellOff;
    private String note;
    private Item item;

    public BoughtItem() {
    }

    public BoughtItem(String id, double price, int quantity, double sellOff, String note, Item item) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.sellOff = sellOff;
        this.note = note;
        this.item = item;
    }

    public double getLineTotal() {
        return Math.max(0, price * quantity - sellOff);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellOff() {
        return sellOff;
    }

    public void setSellOff(double sellOff) {
        this.sellOff = sellOff;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
