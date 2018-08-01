package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Order;
import java.util.List;
import java.util.Map;

public interface Inno72OrderMapper extends Mapper<Inno72Order> {
	Inno72Order selectByRefOrderId(String orderId);

	List<Inno72Order> findGoodsStatusSucc(Map<String, String> orderParams);
}