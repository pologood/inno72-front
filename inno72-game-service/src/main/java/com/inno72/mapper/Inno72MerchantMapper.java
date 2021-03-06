package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Merchant;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface Inno72MerchantMapper extends Mapper<Inno72Merchant> {
	String selectBoundNameByActivityId(String id);

	String selectShopCodeByPlanId(String activityPlanId);

    List<Inno72Merchant> findMerchantByName(@Param("merchantName") String merchantName);

    Inno72Merchant findMerchantByByGoodsId(@Param("goodsId") String goodsId);

    Inno72Merchant findByGoodsId(String goodsId);

	Inno72Merchant findByGoodsCode(String goodsCode);

    Inno72Merchant findByCoupon(String itemId);

	Inno72Merchant findMerchantByMap(Map<String, Object> params);

	Inno72Merchant findMerchantByActivityId(String activityId);

	Inno72Merchant selectByMachineCode(String code);

    Inno72Merchant findByMerchantCode(@Param("merchantCode") String appId);
}