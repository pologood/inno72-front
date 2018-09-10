package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Order;

public interface Inno72OrderMapper extends Mapper<Inno72Order> {
	Inno72Order selectByRefOrderId(String orderId);

	List<Inno72Order> findGoodsStatusSucc(Map<String, String> orderParams);

	Integer findGoodsStatusSuccWithoutUserId(Map<String, String> orderParams);

}