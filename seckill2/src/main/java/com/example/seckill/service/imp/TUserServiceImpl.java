package com.example.seckill.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.mapper.TUserMapper;
import com.example.seckill.pojo.TUser;
import com.example.seckill.service.ITUserService;
import com.example.seckill.utils.CookieUtils;
import com.example.seckill.utils.MD5Util;
import com.example.seckill.utils.UUIDUtil;
import com.example.seckill.vo.LoginVo;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kenly
 * @since 2023-12-20
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

    @Autowired
    private TUserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public RespBean dologin(LoginVo loginvo, HttpServletRequest request, HttpServletResponse response) {

        String mobile = loginvo.getMobile();
        String password = loginvo.getPassword();
//        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || !ValidatorUtill.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.ERROR_login);
//        }
        TUser user = userMapper.selectById(mobile);
        if(null==user){
            throw new GlobalException(RespBeanEnum.ERROR_noUser);
        }
        if(!MD5Util.BackendPassToDB(password,user.getSlat()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.ERROR_login);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();

        redisTemplate.opsForValue().set("user:"+ticket,user);
//        request.getSession().setAttribute(ticket,user);
        CookieUtils.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    @Override
    public TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        TUser user =(TUser) redisTemplate.opsForValue().get("user:" + userTicket);
        if(null == user){
            return null;
        }
        CookieUtils.setCookie(request,response,"userTicket",userTicket);
        return user;
    }
}
