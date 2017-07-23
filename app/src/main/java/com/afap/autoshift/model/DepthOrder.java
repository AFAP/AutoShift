package com.afap.autoshift.model;

/**
 * 按照不同深度归集的订单.
 */

public class DepthOrder {
    private double price;
    private double amount;


    public DepthOrder(double price, double amount) {
        this.price = price;
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "DepthOrder{" +
                "price=" + price +
                ", amount=" + amount +
                '}';
    }
}