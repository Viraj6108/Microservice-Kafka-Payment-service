package com.payment.payment.service;

import com.payment.payment.entity.Payment;
import com.payment.payment.exception.PaymentException;

public interface PaymentService {
    public void pendingPayment(String order) throws PaymentException;
    public Payment proceedPayment(Payment payment) throws PaymentException;
}
