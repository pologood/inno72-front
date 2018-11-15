package com.inno72.service;

import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72MerchantTotalCountByDay;


/**
 * Created by CodeGenerator on 2018/11/08.
 */
public interface Inno72MerchantTotalCountByDayService extends Service<Inno72MerchantTotalCountByDay> {

	Result<Object> searchData(String label, String activityId, String city, String startDate, String endDate,
			String goods, String sellerId);

	byte[] getBytes(Map<String, Object> data, String body, String label);
}
