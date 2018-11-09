package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.mapper.Inno72MerchantTotalCountByDayMapper;
import com.inno72.model.Inno72MerchantTotalCountByDay;
import com.inno72.service.Inno72MerchantTotalCountByDayService;


/**
 * Created by CodeGenerator on 2018/11/08.
 */
@Service
@Transactional
public class Inno72MerchantTotalCountByDayServiceImpl extends AbstractService<Inno72MerchantTotalCountByDay>
		implements Inno72MerchantTotalCountByDayService {
	@Resource
	private Inno72MerchantTotalCountByDayMapper inno72MerchantTotalCountByDayMapper;

	@Override
	public Result<Object> searchData(String label, String activityId, String city, String startDate, String endDate) {

		return null;
	}
}
