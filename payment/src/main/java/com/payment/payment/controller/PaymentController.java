package com.payment.payment.controller;

import com.payment.payment.entity.Payment;
import com.payment.payment.exception.PaymentException;
import com.payment.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/proceed")
    public String proceedPayment(@RequestBody Payment payment)throws PaymentException
    {
        paymentService.proceedPayment(payment);
        return "Success";
    }
}
