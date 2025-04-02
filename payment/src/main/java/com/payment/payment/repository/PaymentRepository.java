package com.payment.payment.repository;

import com.payment.payment.entity.Payment;
import com.payment.payment.exception.PaymentException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {

    @Query(name = "select * from payment p where p.orderId :paymentid")
    Payment findByOrderId(@Param("paymentid") Integer paymentId)throws PaymentException;
}
