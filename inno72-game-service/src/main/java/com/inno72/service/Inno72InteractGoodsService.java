package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72InteractGoods;

public interface Inno72InteractGoodsService extends Service<Inno72InteractGoods> {
    Inno72InteractGoods findByInteractIdAndGoodsId(String interactId, String goodsId);
}
