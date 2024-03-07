package com.example.seckill.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.mapper.GoodsMapper;
import com.example.seckill.pojo.Goods;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kenly
 * @since 2024-03-03
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> findGoodVo() {
        return goodsMapper.findGoodVo();
    }

    @Override
    public GoodsVo findGoodVoByGoodsId(Long GoodsId) {
        return goodsMapper.findGoodVoByGoodsId(GoodsId);
    }
}
