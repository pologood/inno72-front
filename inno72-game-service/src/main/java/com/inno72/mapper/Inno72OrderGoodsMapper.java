package com.inno72.mapper;

import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72OrderGoods;

public interface Inno72OrderGoodsMapper extends Mapper<Inno72OrderGoods> {
	Inno72OrderGoods selectByOrderIdAndGoodsType(Map<String, Object> goodsParams);
}