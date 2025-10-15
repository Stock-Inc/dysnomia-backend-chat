package org.example.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange topic(){
        return new TopicExchange("topic");
    }

    @Bean
    public Queue queue1(){
        return new Queue("history", true);
        }
    @Bean
    public Queue queue2(){
        return new Queue("message", true);
    }

    @Bean
    public Binding binding1a(TopicExchange topic, Queue queue1){
        return BindingBuilder.bind(queue1()).to(topic).with("history");
    }

    @Bean
    public Binding binding2a(TopicExchange topic, Queue queue2){
        return BindingBuilder.bind(queue2()).to(topic).with("message");
    }
}
