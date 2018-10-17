package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Merchant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Inno72MerchantMapper extends Mapper<Inno72Merchant> {
	String selectBoundNameByActivityId(String id);

	String selectShopCodeByPlanId(String activityPlanId);

    List<Inno72Merchant> findMerchantByName(@Param("merchantName") String merchantName);

    Inno72Merchant findMerchantByByGoodsId(@Param("goodsId") String goodsId);
}