package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72SupplyChannel;

public interface Inno72SupplyChannelMapper extends Mapper<Inno72SupplyChannel> {

	int subCount(Inno72SupplyChannel supplyChannel);

	List<Inno72SupplyChannel> selectListByParam(Map<String, Object> map);

}