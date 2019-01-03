package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72StoreExpress;

public interface Inno72StoreExpressMapper extends Mapper<Inno72StoreExpress> {
	void insertS(List<Inno72StoreExpress> insertExpress);
}