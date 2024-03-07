package com.example.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill.pojo.Goods;
import com.example.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kenly
 * @since 2024-03-03
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodVo();

    GoodsVo findGoodVoByGoodsId(Long GoodsId);
}
