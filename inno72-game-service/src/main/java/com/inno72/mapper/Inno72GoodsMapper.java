package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Goods;
import com.inno72.vo.Inno72SamplingGoods;

public interface Inno72GoodsMapper extends Mapper<Inno72Goods> {

	Inno72Goods selectByCode(String code);

	List<Inno72SamplingGoods> selectSamplingGoods(String machineCode);

	Inno72SamplingGoods selectShopInfo(Map<String, String> param);

	Inno72Goods selectByChannelId(String channelId);
}