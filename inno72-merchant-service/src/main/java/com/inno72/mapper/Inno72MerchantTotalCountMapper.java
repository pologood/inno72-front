package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MerchantTotalCount;
import com.inno72.vo.Inno72MerchantTotalCountVo;

public interface Inno72MerchantTotalCountMapper extends Mapper<Inno72MerchantTotalCount> {

	List<Inno72MerchantTotalCountVo> selectByMerchantId(String merchantId);

	String selectActivityType(String activityId);

	int researchFromInteract(String activityId);

	void updateActivityType(@Param("activityId") String activityId, @Param("activityType")String activityType);

	String selectChannelCode(String activityId);

	Inno72MerchantTotalCountVo selectMaxMinTime(String activityId);

	List<Map<String,String>> selectMachineNumCity( @Param("activityId")String activityId,  @Param("merchantId")String merchantId);
}