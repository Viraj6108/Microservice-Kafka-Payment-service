package com.payment.payment.serviceImpl;

import com.google.gson.Gson;
import com.payment.payment.entity.Orders;
import com.payment.payment.entity.Payment;
import com.payment.payment.exception.PaymentException;
import com.payment.payment.repository.PaymentRepository;
import com.payment.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
        System.out.println("📩 New Payment Received in Payment Service: " + payment);
        Payment payment1 = paymentRepository.findByOrderId(payment.getOrderId());
        if(payment1.getStatus().equals(Payment.STATUS.SUCCESS))
        {
            throw new PaymentException("Payment Already Done ");
        }
        payment1.setStatus(Payment.STATUS.SUCCESS);
        payment1.setPaymentMode(Payment.PaymentMode.ONLINE);
        paymentRepository.save(payment1);
        String paymentJson = gson.toJson(payment1);
        //An event will be produced once payment is done
        kafkaTemplate.send("payment-event",paymentJson);
        return payment1;
    }
}
