package com.example.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//实验性质的类，正式实现在RaabitMQTOPICConfig
@Configuration
public class RabbitMQConfig {
    /*
    private static final String EXCHANGE = "fanout_01";
    private static final String DIRECT = "direct";
    private static final String TOPIC = "topic";

    @Bean
    public Queue queue(){
        return new Queue("queue",true);
    }

    //使用fanout模式的mq
    @Bean
    public Queue queue01(){
        return new Queue("queue01");
    }

    @Bean
    public Queue queue02(){
        return new Queue("queue02");
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(fanoutExchange());
    }

    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(fanoutExchange());
    }

    //使用Direct模式的mq

    @Bean
    public Queue queue03(){
        return new Queue("queue03");
    }

    @Bean
    public Queue queue04(){
        return new Queue("queue04");
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(DIRECT);
    }

    @Bean
    public Binding binding03(){
        return BindingBuilder.bind(queue03()).to(directExchange()).with("direct_red");
    }

    @Bean
    public Binding binding04(){
        return BindingBuilder.bind(queue04()).to(directExchange()).with("direct_blue");
    }

    //使用topic模式mq

    @Bean
    public Queue queue05(){
        return new Queue("queue05");
    }

    @Bean
    public Queue queue06(){
        return new Queue("queue06");
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC);
    }

    @Bean
    public Binding binding05(){
        return BindingBuilder.bind(queue05()).to(topicExchange()).with("#.queue.#");
    }

    @Bean
    public Binding binding06(){
        return BindingBuilder.bind(queue06()).to(topicExchange()).with("*.queue.#");
    }

     */
}
