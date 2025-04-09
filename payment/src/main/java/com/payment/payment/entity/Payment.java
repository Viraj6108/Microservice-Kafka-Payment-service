package com.payment.payment.entity;

import jakarta.persistence.*;


@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    public  enum STATUS{
        SUCCESS, FAILED, PENDING,REFUND
    }
    @Enumerated(EnumType.STRING)
    private STATUS status;
    public enum PaymentMode {
        COD, ONLINE
    }
    @Enumerated(EnumType.STRING) // Store as a string in the database
    private PaymentMode paymentMode;

    private Integer orderId;

    public Payment() {
    }

    public Payment(Integer paymentId, STATUS status,PaymentMode paymentMode) {
        this.paymentId = paymentId;
        this.status = status;
        this.paymentMode = paymentMode;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;

    }
    public PaymentMode getPaymentMode( ){
        return paymentMode;
    }
    public void setPaymentMode(PaymentMode paymentMode)
    {
        this.paymentMode = paymentMode;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
