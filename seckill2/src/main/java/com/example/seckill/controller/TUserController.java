package com.example.seckill.controller;


import com.example.seckill.pojo.TUser;
import com.example.seckill.rabbitmq.MQSender;
import com.example.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * windowQPS:2961.2
 * @author kenly
 * @since 2023-12-20
 */
@RestController
@RequestMapping("/user")
public class TUserController{

//    @Autowired
//    private MQSender mqSender;
//
//    @RequestMapping("/info")
//    public RespBean info(TUser user){
//        return RespBean.success();
//    }
//
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqSender.send("hello,发送消息");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq01(){
//        mqSender.fanoutSend("fanout模式发送消息");
//    }
//
//    @RequestMapping("/mq/direct")
//    @ResponseBody
//    public void mq02(){
//        mqSender.directSend("direct模式发送消息");
//    }
//
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public void mq03(){
//        mqSender.topicSend("topic模式发送消息");
//    }
}
