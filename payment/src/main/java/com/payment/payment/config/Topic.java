package com.payment.payment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class Topic {

    @Bean
    public NewTopic payment()
    {
        return TopicBuilder.name("payment-event").build();
    }

    @Bean
    public NewTopic paymetRefund()
    {
        return TopicBuilder.name("payment-refund").build();
    }

    @Bean
    public NewTopic paymentorder(){return TopicBuilder.name("payment-order").build();}
}
