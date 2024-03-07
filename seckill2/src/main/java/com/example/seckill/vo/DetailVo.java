package com.example.seckill.vo;

import com.example.seckill.pojo.TUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {
    private TUser user;

    private GoodsVo goodsVo;

    private int secKillStatus;

    private int remainSeconds;
}
