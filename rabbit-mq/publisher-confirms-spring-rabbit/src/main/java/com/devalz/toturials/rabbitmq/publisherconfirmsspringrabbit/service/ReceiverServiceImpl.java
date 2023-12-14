package com.devalz.toturials.rabbitmq.publisherconfirmsspringrabbit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReceiverServiceImpl implements ReceiverService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiverServiceImpl.class);

    @RabbitListener(queues = {"${app.queue.name}"})
    public void onReceive(String message) {
        logger.info("message receive successfully : {} ", message);
    }
}
