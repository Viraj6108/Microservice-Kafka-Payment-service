package com.payment.payment.serviceImpl;

import com.google.gson.Gson;
import com.payment.payment.entity.Orders;
import com.payment.payment.entity.Payment;
import com.payment.payment.exception.PaymentException;
import com.payment.payment.repository.PaymentRepository;
import com.payment.payment.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private KafkaTemplate kafkaTemplate ;
    public PaymentServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    private final Gson gson = new Gson();
    @KafkaListener(topics = "order-event",groupId ="payment-group")
    @Override
    public void pendingPayment(String orderJson) throws PaymentException {
        Orders orders= gson.fromJson(orderJson, Orders.class);
        System.out.println("📩 New Order Received in Payment Service: " + orderJson);
        System.out.println("📩 New Order Received in Payment Service: " + orders);
        Payment payment = new Payment();
        //Payment will set to pending until payment api is accessed
        try{
            payment.setPaymentMode(Payment.PaymentMode.ONLINE);
            payment.setStatus(Payment.STATUS.PENDING);
            payment.setOrderId(orders.getOrderId());
            paymentRepository.save(payment);
        }catch (Exception e)
        {
            throw new PaymentException("Payment not Successful");
        }
    }

    @Override
    public Payment proceedPayment(Payment payment) throws PaymentException {
        Random random = new Random();
        int number = random.nextInt(0,90);
        System.out.println("📩 New Payment Received in Payment Service: " + payment);
        Payment payment1 = paymentRepository.findByOrderId(payment.getOrderId());
        if(payment1.getStatus().equals(Payment.STATUS.SUCCESS))
        {
            throw new PaymentException("Order is placed ");
        }
        if(number <=30)
        {
            payment1.setStatus(Payment.STATUS.SUCCESS);
            payment1.setPaymentMode(payment.getPaymentMode());
        }
        if(number <=60 && number >=31)
        {
            payment1.setStatus(Payment.STATUS.FAILED);
            payment1.setPaymentMode(payment.getPaymentMode());
            throw new PaymentException("Insufficient balance ");
        }
        if(number>61 && number<=90)
        {
            payment1.setStatus(Payment.STATUS.PENDING);
            payment1.setPaymentMode(payment.getPaymentMode());
            throw new PaymentException("Server down . Please try again later");
        }
        paymentRepository.save(payment1);
        String paymentJson = gson.toJson(payment1);
        //An event will be produced once payment is done
        kafkaTemplate.send("payment-event",paymentJson);
        return payment1;
    }

    //Cancelled order set status to REFUND
    @KafkaListener(topics = "order-cancel", groupId = "payment-group")
    @Transactional
    @Override
    public void RefundPayment(String paymentJson) throws PaymentException {

            Orders order = gson.fromJson(paymentJson, Orders.class);
            System.out.println("🔍 Extracted Order ID: " + order.getOrderId());
                Payment payment = paymentRepository.findByOrderId(order.getOrderId());
                if (payment == null) {
                    throw new PaymentException("No payment record found for Order ID: " + order.getOrderId());
                }
                System.out.println("✅ Found payment record: " + payment);
                System.out.println("🔍 Current Payment Status: " + payment.getStatus());
                if (payment.getStatus().equals(Payment.STATUS.FAILED)) {
                    throw new PaymentException("Due to failure in payment order is not confirmed");
                }
                payment.setStatus(Payment.STATUS.REFUND);
                paymentRepository.save(payment);
                System.out.println("✅ Updated Payment: " + payment);
                String paymentJson1 = gson.toJson(payment);
                kafkaTemplate.send("payment-refund", paymentJson1);


    }
}
