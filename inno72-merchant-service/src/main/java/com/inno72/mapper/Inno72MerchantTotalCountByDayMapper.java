package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MerchantTotalCountByDay;

public interface Inno72MerchantTotalCountByDayMapper extends Mapper<Inno72MerchantTotalCountByDay> {
	List<Inno72MerchantTotalCountByDay> selectList(String activityId, String city, String startDate, String endDate);
}