package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.Goods;
import com.example.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kenly
 * @since 2024-03-03
 */
public interface IGoodsService extends IService<Goods> {
    List<GoodsVo> findGoodVo();

    GoodsVo findGoodVoByGoodsId(Long GoodsId);
}
