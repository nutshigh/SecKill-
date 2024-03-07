package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.TUser;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kenly
 * @since 2024-03-03
 */
public interface IOrderService extends IService<Order> {
    Order secKill(TUser user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

    String creatPath(TUser user, Long goodsId);

    boolean checkPah(TUser user,Long goodsId, String path);
}
