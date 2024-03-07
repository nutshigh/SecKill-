package com.example.seckill.controller;

import com.example.seckill.service.ITUserService;
import com.example.seckill.vo.LoginVo;
import com.example.seckill.vo.RespBean;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller //RestController会加上ResponseBody,会返回对象而不是做页面跳转
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private ITUserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/dologin")
    @ResponseBody
    public RespBean dologin(@Valid LoginVo loginvo, HttpServletRequest request, HttpServletResponse response){
        return userService.dologin(loginvo,request,response);
    }
}
