package com.expense_manager.service.AmqpServices;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class MessageConsumer {
      
    @RabbitListener(queues = "myQueue")
    public <T> void receivedMessage(Class<T> entityClass){
        System.out.println("message is : "+entityClass.toString());
    }
}
