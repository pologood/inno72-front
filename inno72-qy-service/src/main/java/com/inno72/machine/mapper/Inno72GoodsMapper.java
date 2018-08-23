package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72Goods;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Inno72GoodsMapper extends Mapper<Inno72Goods> {
	

	List<Inno72Goods> getLackGoods(String checkUserId);

	List<Inno72Goods> selectByMachineId(String machineId);
}