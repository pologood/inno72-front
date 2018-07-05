package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72GameResultGoods;

public interface Inno72GameResultGoodsMapper extends Mapper<Inno72GameResultGoods> {

	List<String> findGoodsId(Map<String, String> requestParam);
}