package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MerchantTotalCountByDay;

public interface Inno72MerchantTotalCountByDayMapper extends Mapper<Inno72MerchantTotalCountByDay> {
	List<Inno72MerchantTotalCountByDay> selectList(@Param("activityId") String activityId, @Param("city") String city,
			@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("goods") String goods,
			@Param("merchantId") String merchantId);

	List<Map<String, String>> findGoodsByMerchantId(String merchantId);

	List<Map<String, String>> findActivityByMerchantId(String merchantId);

}