package org.example.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange topic() {
        return new TopicExchange("topic");
    }

    @Bean
    public Queue queue1() {
        return new Queue("history", true);
    }

    @Bean
    public Queue queue2() {
        return new Queue("message", true);
    }

    @Bean
    public Queue privateQueue() {
        return new Queue("private", true);
    }

    @Bean
    public Binding binding1a(TopicExchange topic, Queue queue1) {
        return BindingBuilder.bind(queue1()).to(topic).with("history");
    }

    @Bean
    public Binding binding2a(TopicExchange topic, Queue queue2) {
        return BindingBuilder.bind(queue2()).to(topic).with("message");
    }

    @Bean
    public Binding privateBinding(TopicExchange topic, Queue privateQueue) {
        return BindingBuilder.bind(privateQueue()).to(topic).with("private");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
