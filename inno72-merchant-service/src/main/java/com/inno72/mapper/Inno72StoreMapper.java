package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Store;

public interface Inno72StoreMapper extends Mapper<Inno72Store> {
	List<Inno72Store> selectAllOrderCreateTime();

}