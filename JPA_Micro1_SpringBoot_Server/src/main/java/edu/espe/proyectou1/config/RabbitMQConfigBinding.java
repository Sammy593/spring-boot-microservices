package edu.espe.proyectou1.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfigBinding {

    public static final String QUEUE_A = "queue.A";
    public static final String QUEUE_B = "queue.B";
    public static final String QUEUE_C = "queue.C";
    public static final String ROUTING_A = "routing.A";
    public static final String ROUTING_B = "routing.B";
    public static final String ROUTING_C = "routing.C";

    @Bean
    Queue queueA(){
         return new Queue(QUEUE_A, false);
     }

    @Bean
    Queue queueB(){
        return new Queue(QUEUE_B, false);
    }

    @Bean
    Queue queueC(){
        return new Queue(QUEUE_C, false);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange("exchange.direct");
     }

    @Bean
    Binding binding(Queue queueA, DirectExchange exchange){
        return BindingBuilder.bind(queueA)
                .to(exchange)
                .with(ROUTING_A);
    }

    @Bean
    Binding bindingB(Queue queueB, DirectExchange exchange){
        return BindingBuilder.bind(queueB)
                .to(exchange)
                .with(ROUTING_B);
    }

    @Bean
    Binding bindingC(Queue queueC, DirectExchange exchange){
        return BindingBuilder.bind(queueC)
                .to(exchange)
                .with(ROUTING_C);
    }

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
