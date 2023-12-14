package com.devalz.toturials.rabbitmq.publisherconfirmsspringrabbit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Value("${app.queue.name}")
    private String queueName;

    @Bean
    public Queue queue() {
        return new Queue(queueName, false, false, true);
    }

    @Bean
    public RabbitTemplate customRabbitTemplate(RabbitTemplateConfigurer configurer,
                                               ConnectionFactory connectionFactory,
                                               ObjectProvider<RabbitTemplateCustomizer> customizers) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        configurer.configure(rabbitTemplate, connectionFactory);
        customizers.orderedStream().forEach((customizer) -> customizer.customize(rabbitTemplate));
        rabbitTemplate.setConfirmCallback(new ConfirmCallbackImpl());
        rabbitTemplate.setReturnsCallback(returnedMessage -> logger.info(returnedMessage.toString()));
        return rabbitTemplate;
    }

    public static class ConfirmCallbackImpl implements RabbitTemplate.ConfirmCallback {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            //read "Appropriate actions for confirm callback" section in readme file
            String correlationDataId = correlationData == null ? "unknown correlationId" : correlationData.getId();
            logger.info(String.format("confirm callback -> ack is %s for correlationId %s", ack, correlationDataId));
        }
    }

}
