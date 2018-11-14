package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72MerchantTotalCountByDay;
import com.inno72.service.Inno72MerchantTotalCountByDayService;

/**
 * Created by CodeGenerator on 2018/11/08.
 */
@RestController
@RequestMapping("/inno72/merchant/total/count/by/day")
@CrossOrigin
public class Inno72MerchantTotalCountByDayController {
	@Resource
	private Inno72MerchantTotalCountByDayService inno72MerchantTotalCountByDayService;

	@RequestMapping(value = "/search/{label}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result searchData(@PathVariable(value = "label") String label, String activityId, String city, String startDate, String endDate) {
		return inno72MerchantTotalCountByDayService
				.searchData(label, activityId, city, startDate, endDate);
	}

}
