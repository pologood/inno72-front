package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72StoreOrder;

public interface Inno72StoreOrderMapper extends Mapper<Inno72StoreOrder> {
	List<Map<String, String>> findStoreOrder(@Param("merchantId") String merchantId,@Param("activityId") String activityId);

	void insertS(List<Inno72StoreOrder> list);
}