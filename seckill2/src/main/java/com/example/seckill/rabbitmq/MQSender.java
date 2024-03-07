package com.example.seckill.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSeckillMessage(String msg){
        log.info("发送信息"+msg);
        rabbitTemplate.convertAndSend("seckillExchange","seckill",msg);
    }

//    public void send(Object msg){
//        log.info("发送消息,"+msg);
//        rabbitTemplate.convertAndSend("queue",msg);
//    }
//
//    public void fanoutSend(Object msg){
//        log.info("fanout模式发送消息:"+msg);
//        rabbitTemplate.convertAndSend("fanout_01","",msg);
//    }
//
//    public void directSend(Object msg){
//        log.info("direct模式发送消息："+msg);
//        rabbitTemplate.convertAndSend("direct","direct_red",msg);
//    }
//
//    public void topicSend(Object msg){
//        log.info("topic模式发送消息");
//        rabbitTemplate.convertAndSend("topic","z.x.queue",msg);
//        rabbitTemplate.convertAndSend("topic","q.queue","两个都能接收到的消息："+ msg);
//    }

}
