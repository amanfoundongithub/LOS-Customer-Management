package com.loan_org.customer_management.config;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.loan_org.customer_management.properties.RabbitMQProperties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitMQProperties.class)
@ConditionalOnProperty(
        prefix = "event.rabbitmq",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class RabbitMQConfig {

    private final RabbitMQProperties properties;

    @Bean TopicExchange customerEventExchange() {
        RabbitMQProperties.Exchange config =properties.getExchange();
        return new TopicExchange(
                config.getName(),
                config.isDurable(),
                config.isAutoDelete()
        );
    }

    @Bean RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        configurePublisher(connectionFactory);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new JacksonJsonMessageConverter());
        return rabbitTemplate;
    }

    private void configurePublisher(CachingConnectionFactory connectionFactory) {
        RabbitMQProperties.Publisher config = properties.getPublisher();
        if (!config.isEnabled()) {
            return;
        }
        connectionFactory.setPublisherConfirmType(
                config.isConfirms()
                        ? CachingConnectionFactory.ConfirmType.CORRELATED
                        : CachingConnectionFactory.ConfirmType.NONE
        );
        connectionFactory.setPublisherReturns(
                config.isReturns()
        );
    }
}