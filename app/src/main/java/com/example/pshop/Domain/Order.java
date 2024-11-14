package com.example.pshop.Domain;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private int productCount;
    private double totalAmount;
    private String accountNumber;
    private String bankName;
    private String accountName;
    private String orderIdKey;
    private String status;
    private List<ItemsDomain> itemsList;

    // Constructor không tham số
    public Order() {
    }

    // Constructor có tham số
    public Order(String orderId, int productCount, double totalAmount, String accountNumber, String bankName,
                 String accountName, String orderIdKey, String status, List<ItemsDomain> itemsList) {
        this.orderId = orderId;
        this.productCount = productCount;
        this.totalAmount = totalAmount;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.accountName = accountName;
        this.orderIdKey = orderIdKey;
        this.status = status;
        this.itemsList = itemsList;
    }

    // Getter và Setter cho các thuộc tính
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOrderIdKey() {
        return orderIdKey;
    }

    public void setOrderIdKey(String orderIdKey) {
        this.orderIdKey = orderIdKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemsDomain> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<ItemsDomain> itemsList) {
        this.itemsList = itemsList;
    }
}
