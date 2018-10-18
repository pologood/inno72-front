package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72InteractGoodsMapper;
import com.inno72.model.Inno72InteractGoods;
import com.inno72.service.Inno72InteractGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Inno72InteractGoodsServiceImpl extends AbstractService<Inno72InteractGoods> implements Inno72InteractGoodsService {
    @Autowired
    private Inno72InteractGoodsMapper mapper;
    @Override
    public Inno72InteractGoods findByInteractIdAndGoodsId(String interactId, String goodsId) {
        Inno72InteractGoods param = new Inno72InteractGoods();
        param.setGoodsId(goodsId);
        param.setInteractId(interactId);
        return mapper.selectOne(param);
    }
}
