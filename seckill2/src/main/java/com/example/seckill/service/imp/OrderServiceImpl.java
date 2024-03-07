package com.example.seckill.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.mapper.OrderMapper;
import com.example.seckill.mapper.SeckillOrdersMapper;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.pojo.SeckillOrders;
import com.example.seckill.pojo.TUser;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillGoodsService;
import com.example.seckill.service.ISeckillOrdersService;
import com.example.seckill.utils.MD5Util;
import com.example.seckill.utils.UUIDUtil;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.OrderDetailVo;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kenly
 * @since 2024-03-03
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private ISeckillOrdersService seckillOrdersService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IGoodsService goodsService;

    @Transactional
    @Override
    public Order secKill(TUser user, GoodsVo goods) {
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//        boolean seckillGoodResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().set("stock_count",
//                seckillGoods.getStockCount()).eq("id",seckillGoods.getGoodsId()).gt("stock_count",0));
//                更新不能解决超卖问题，或许与更新语句特性有关？version 2 直接更新，很多进程可能获取相同的大于0的count，直接设置数据库会加锁（猜测）
        boolean seckillGoodResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count - 1")
                .eq("id",seckillGoods.getGoodsId()).gt("stock_count",0));//该为直接写数据库 version 3
//        seckillGoodsService.updateById(seckillGoods);version 1
        if(!seckillGoodResult){
            return  null;
        }
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(goods.getGoodsStock());
        order.setGoodsPrice(goods.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        SeckillOrders seckillOrder = new SeckillOrders();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrdersService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder);
        return order;
    }

    public OrderDetailVo detail(Long orderId){
        if(orderId == null){
            throw new GlobalException(RespBeanEnum.NO_ORDER);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goods = goodsService.findGoodVoByGoodsId(order.getGoodsId());
        return new OrderDetailVo(order,goods);
    }

    @Override
    public String creatPath(TUser user, Long goodsId) {
        String path = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisTemplate.opsForValue().set("seckillPath:"+ user.getId() +":"+goodsId,path,60, TimeUnit.SECONDS);
        return path;
    }

    @Override
    public boolean checkPah(TUser user, Long goodsId, String path) {
        if(null==user){
            return false;
        }
        String generatedPath =(String) redisTemplate.opsForValue().get("seckillPath:"+ user.getId() +":"+goodsId);
        return generatedPath.equals(path);
    }
}
