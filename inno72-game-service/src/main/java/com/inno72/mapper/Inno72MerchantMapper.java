package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Merchant;

public interface Inno72MerchantMapper extends Mapper<Inno72Merchant> {
	String selectBoundNameByActivityId(String id);

	String selectShopCodeByPlanId(String activityPlanId);
}