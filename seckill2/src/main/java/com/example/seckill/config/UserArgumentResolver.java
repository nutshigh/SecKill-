package com.example.seckill.config;

import com.example.seckill.pojo.TUser;
import com.example.seckill.service.ITUserService;
import com.example.seckill.utils.CookieUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private ITUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return TUser.class == parameter.getParameterType();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        String Cookie = CookieUtils.getCookieValue(request,"userTicket");
        if(StringUtils.isEmpty(Cookie)){
            return null;
        }
        TUser user = userService.getUserByCookie(Cookie,request,response); //从redis中取数据
        return user;
    }
}
