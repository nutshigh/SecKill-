package com.example.seckill.vo;


import com.example.seckill.pojo.TUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {
    TUser user;
    Long goodsId;
}
