package com.payment.payment.entity;

public class Orders {
    private Integer orderId;
    private String item;
    private Integer quantity;
    private double price;
    private String email;
    private String address;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        orderId = orderId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "OrderId=" + orderId +
                ", item='" + item + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}
