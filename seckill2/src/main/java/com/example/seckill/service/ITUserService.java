package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.TUser;
import com.example.seckill.vo.LoginVo;
import com.example.seckill.vo.RespBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;



/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kenly
 * @since 2023-12-20
 */

public interface ITUserService extends IService<TUser> {

    RespBean dologin(LoginVo loginvo, HttpServletRequest request, HttpServletResponse response);

    TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);
}
