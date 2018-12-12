package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MerchantTotalCount;

public interface Inno72MerchantTotalCountMapper extends Mapper<Inno72MerchantTotalCount> {

	List<Inno72MerchantTotalCount> selectByMerchantId(String merchantId);

	String selectActivityType(String activityId);

	int researchFromInteract(String activityId);

	void updateActivityType(@Param("activityId") String activityId, @Param("activityType")String activityType);
}