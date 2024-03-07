package com.example.seckill.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    ERROR_login(50010,"用户名或密码错误"),
    ERROR_noUser(50011,"用户不存在"),
    ERROR_BIND(500212,"参数校验错误"),
    //秒杀模块
    EMPTY_STOCK(500500,"库存不足"),
    REPEAT_ERROR(500510,"多次下单"),
    NO_ORDER(500511,"订单不存在"),
    WAITING(0,"等待秒杀结果"),
    REQUEST_ILLEGAL(500512,"请求非法");
    private final Integer code;
    private final String message;
}
