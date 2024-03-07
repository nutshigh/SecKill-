package com.example.seckill.controller;


import com.example.seckill.pojo.TUser;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ITUserService;
import com.example.seckill.vo.OrderDetailVo;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kenly
 * @since 2024-03-03
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    ITUserService userService;


    @Autowired
    IOrderService orderService;

    @Autowired
    IGoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(TUser user, Long orderId){
        if(user == null){
            return RespBean.error(RespBeanEnum.ERROR_noUser);
        }
        OrderDetailVo orderDetailVo = orderService.detail(orderId);
        return RespBean.success(orderDetailVo);
    }

}
