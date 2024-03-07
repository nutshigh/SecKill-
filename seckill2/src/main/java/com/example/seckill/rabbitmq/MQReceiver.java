package com.example.seckill.rabbitmq;


import com.example.seckill.pojo.SeckillOrders;
import com.example.seckill.pojo.TUser;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.imp.OrderServiceImpl;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import com.example.seckill.vo.SeckillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IOrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg){
        log.info("接收到秒杀消息"+msg);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(msg,SeckillMessage.class);
        TUser user = seckillMessage.getUser();
        Long goodsId = seckillMessage.getGoodsId();
        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        if(goodsVo.getStockCount() < 1){
            return;
        }
        SeckillOrders seckillOrders = (SeckillOrders)redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if (seckillOrders != null) {
            return;
        }
        orderService.secKill(user,goodsVo);
    }

//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue01")
//    public void receive01(Object msg){
//        log.info("queue01接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue02")
//    public void receive02(Object msg){
//        log.info("queue02接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue03")
//    public void receive03(Object msg){
//        log.info("queue03接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue05")
//    public void receive05(Object msg){
//        log.info("queue05接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue06")
//    public void receive06(Object msg){
//        log.info("queue06接收消息："+msg);
//    }
}
