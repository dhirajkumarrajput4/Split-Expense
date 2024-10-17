package com.expense_manager.service.AmqpServices;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense_manager.comman.Amqpkey;


@Service
public class MessageProducer {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public <T> void sendMessage(T entityClass) {
        rabbitTemplate.convertAndSend(Amqpkey.DirectExchange.getValue(),Amqpkey.RoutingKey.getValue(),entityClass);
    }

}
