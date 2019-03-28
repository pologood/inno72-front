package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Order;
import com.inno72.vo.OrderOrderGoodsVo;
import com.inno72.vo.OrderVo;
import org.apache.ibatis.annotations.Param;

public interface Inno72OrderMapper extends Mapper<Inno72Order> {
	Inno72Order selectByRefOrderId(String orderId);

	List<Inno72Order> findGoodsStatusSucc(Map<String, String> orderParams);

	List<Inno72Order> findGoodsStatusSuccPy(Map<String, String> orderParams);

	Integer findGoodsStatusSuccWithoutUserId(Map<String, String> orderParams);

    List<OrderOrderGoodsVo> findSuccessOrderByMerchantId(@Param("merchantId") String merchantId);

    List<OrderVo> orderList(@Param("gameUserId") String gameUserId);

	Map<String,String> findTotalMoney(Map<String, String> param);
}