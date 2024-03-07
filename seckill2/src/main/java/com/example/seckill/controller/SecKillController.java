package com.example.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.SeckillOrders;
import com.example.seckill.pojo.TUser;
import com.example.seckill.rabbitmq.MQSender;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillOrdersService;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import com.example.seckill.vo.SeckillMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//windowsQPS优化前：1次秒杀：522.7 三次秒杀：686.8
//       加入页面缓存QPS：3次秒杀766.2
//       静态页面：3次秒杀1411.8
//       加入MQ优化：3595.8
//

@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IOrderService OrdersService;

    @Autowired
    private ISeckillOrdersService seckillOrdersService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    Map<Long,Boolean> EmptyStockMap = new HashMap<>();

//    @RequestMapping("doSeckill")
//    public String doSecKill(Model model, TUser user, Long goodsId){
//        if(user == null){
//            return "login";
//        }
//        model.addAttribute("user:",user);
//        GoodsVo goods = goodsService.findGoodVoByGoodsId(goodsId);
//        if(goods.getStockCount() <= 0){
//            model.addAttribute("errormsg", RespBeanEnum.EMPTY_STOCK);
//            return "secKillFail";
//        }
//        //根据user_id 和goods_id取出对象
//        SeckillOrders seckillOrders =
//                seckillOrdersService.
//                        getOne(new QueryWrapper<SeckillOrders>().
//                                eq("user_id",user.getId()).eq("goods_id",goodsId));
//        if(seckillOrders != null){
//            model.addAttribute("errmsg",RespBeanEnum.REPEAT_ERROR);
//            return "secKillFail";
//        }
//        Order order = OrdersService.secKill(user,goods);
//        model.addAttribute("orderInfo",order);
//        model.addAttribute("goods",goods);
//        return "orderDetail";
//    }
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(TUser user,Long goodsId){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        SeckillOrders seckillOrders = (SeckillOrders) valueOperations.get("order:"+user.getId()+":"+goodsId);
        System.out.println("sec1"+ seckillOrders);
        if(null != seckillOrders){
            return RespBean.success(seckillOrders.getOrderId()); //这里传入的orderid为Long类型，但是RespBean的code为整型，订单多了可能会溢出，偷个懒不改了
        }else if((Integer)valueOperations.get("seckillGoods:"+goodsId) < 1){
            return RespBean.success(-1);
        }else{
            return RespBean.success(0);
        }
    }

    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path,Model model, TUser user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.ERROR_noUser);
        }
        //判断路径合法性
        boolean check = OrdersService.checkPah(user,goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //判断重复抢购
        SeckillOrders seckillOrders = (SeckillOrders)redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if (seckillOrders != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long goodsStock = valueOperations.decrement("seckillGoods:"+goodsId);
        if(goodsStock < 0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:"+goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user,goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);

        /*
        if (user == null) {
            return RespBean.error(RespBeanEnum.ERROR_noUser);
        }
        GoodsVo goods = goodsService.findGoodVoByGoodsId(goodsId);
        if (goods.getStockCount() <= 0) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
//        SeckillOrders seckillOrders =
//                seckillOrdersService.
//                        getOne(new QueryWrapper<SeckillOrders>().
//                                eq("user_id", user.getId()).eq("goods_id", goodsId));这一行的作用是判断用户是否重复下单，替代方式：
//        通过userid+goodsid建立唯一索引防止用户重复下单（因为同样的索引只准出现一次，也就是只有一行）
        SeckillOrders seckillOrders = (SeckillOrders)redisTemplate.opsForValue().get("order:"+user.getId()+":"+goods.getId());
        if (seckillOrders != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        Order order = OrdersService.secKill(user, goods);
        return RespBean.success(order);
        */
    }

    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(TUser user,Long goodsId){
        if(null == user){
            return RespBean.error(RespBeanEnum.ERROR_noUser);
        }
        String str = OrdersService.creatPath(user,goodsId);
        return RespBean.success(str);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(),false);
        });
    }
}
