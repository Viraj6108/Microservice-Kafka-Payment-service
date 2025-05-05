package com.payment.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PaymentException extends Exception {
    public PaymentException(String msg)
    {
        super(msg);
    }
}
