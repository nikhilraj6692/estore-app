package com.estore.demo.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;

/*
POJO object to hold order information after user has placed the order
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"name"}, allowGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItem {
    @Id
    private String id;

    private String sellerId;

    @Min(1)
    private int qty;

    private double price;

    private double tax;

    private double otherCharges;

    private double totalPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTax() {
        return 0.18 * price;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getOtherCharges() {
        return 0.03 * (this.getPrice() + this.getTax()) + 200;
    }

    public void setOtherCharges(double otherCharges) {
        this.otherCharges = otherCharges;
    }

    public double getTotalPrice() {
        return this.getPrice() + this.getTax() + this.getOtherCharges();
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}