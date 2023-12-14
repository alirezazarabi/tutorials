package com.devalz.toturials.rabbitmq.publisherconfirmsspringrabbit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class PublisherConfirmsSpringRabbitApplicationTests {

    @Value("${app.queue.name}")
    private String queueName;

    @Autowired
    private RabbitTemplate customRabbitTemplate;

    @Test
    void send_validQueueAndExchange_ackIsTrue() throws ExecutionException, InterruptedException, TimeoutException {
        CorrelationData correlationData = new CorrelationData("message1-CorrelationId");
        this.customRabbitTemplate.convertAndSend("", queueName, "message1", correlationData);
        CorrelationData.Confirm confirm = correlationData.getFuture().get(10, TimeUnit.SECONDS);
        Assertions.assertTrue(confirm.isAck());
    }

    @Test
    void send_invalidQueue_ackIsTrue_noRouteReplyCode() throws ExecutionException, InterruptedException, TimeoutException {
        CorrelationData correlationData = new CorrelationData("message2-CorrelationId");
        this.customRabbitTemplate.convertAndSend("", "non-exist-queue", "message2", correlationData);
        CorrelationData.Confirm confirm = correlationData.getFuture().get(10, TimeUnit.SECONDS);
        Assertions.assertTrue(confirm.isAck());
        Assertions.assertEquals(312, correlationData.getReturned().getReplyCode());
        Assertions.assertEquals("NO_ROUTE", correlationData.getReturned().getReplyText());
    }

    @Test
    void send_invalidExchange_ackIsFalse() throws ExecutionException, InterruptedException, TimeoutException {
        CorrelationData correlationData = new CorrelationData("message3-CorrelationId");
        this.customRabbitTemplate.convertAndSend("non-exist-exchange", queueName, "message3", correlationData);
        CorrelationData.Confirm confirm = correlationData.getFuture().get(10, TimeUnit.SECONDS);
        Assertions.assertFalse(confirm.isAck());
    }

}
