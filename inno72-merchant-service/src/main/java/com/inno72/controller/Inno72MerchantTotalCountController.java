package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72MerchantTotalCount;
import com.inno72.service.Inno72MerchantTotalCountService;

/**
 * Created by CodeGenerator on 2018/11/07.
 */
@RestController
@RequestMapping("/inno72/merchant/total/count")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72MerchantTotalCountController {
	@Resource
	private Inno72MerchantTotalCountService inno72MerchantTotalCountService;

	@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
	public Result update(Inno72MerchantTotalCount inno72MerchantTotalCount) {
		inno72MerchantTotalCountService.update(inno72MerchantTotalCount);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
	public Result detail(@RequestParam String id) {
		Inno72MerchantTotalCount inno72MerchantTotalCount = inno72MerchantTotalCountService.findById(id);
		return ResultGenerator.genSuccessResult(inno72MerchantTotalCount);
	}

	@RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<List<Inno72MerchantTotalCount>> list(String id) {
		return inno72MerchantTotalCountService.findAllById(id);
	}
	@RequestMapping(value = "/totle", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<Object> totle(String id) {
		return inno72MerchantTotalCountService.totle(id);
	}
}
