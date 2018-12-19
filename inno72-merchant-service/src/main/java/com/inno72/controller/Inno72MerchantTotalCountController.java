package com.inno72.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.inno72.vo.ActMerchantLog;
import com.inno72.vo.Inno72MerchantTotalCountVo;

/**
 * Created by CodeGenerator on 2018/11/07.
 */
@RestController
@RequestMapping(value = "/inno72/merchant/total/count", method = {RequestMethod.POST, RequestMethod.GET,
		RequestMethod.OPTIONS})
public class Inno72MerchantTotalCountController {
	@Resource
	private Inno72MerchantTotalCountService inno72MerchantTotalCountService;

	@RequestMapping(value = "/update")
	public Result update(Inno72MerchantTotalCount inno72MerchantTotalCount) {
		inno72MerchantTotalCountService.update(inno72MerchantTotalCount);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail")
	public Result detail(@RequestParam String id) {
		Inno72MerchantTotalCount inno72MerchantTotalCount = inno72MerchantTotalCountService.findById(id);
		return ResultGenerator.genSuccessResult(inno72MerchantTotalCount);
	}

	@RequestMapping(value = "/list")
	public Result<List<Inno72MerchantTotalCountVo>> list(String id) {
		return inno72MerchantTotalCountService.findAllById(id);
	}


	@RequestMapping(value = "/addressNum")
	public Result<List<Map<String, Object>>> addressNum(String actId) {
		return inno72MerchantTotalCountService.addressNum(actId);
	}

	@RequestMapping(value = "/actLog")
	public Result<List<ActMerchantLog>> actLog(String actId) {
		return inno72MerchantTotalCountService.actLog(actId);
	}



	@RequestMapping(value = "/totle")
	public Result<Object> totle(String id) {
		return inno72MerchantTotalCountService.totle(id);
	}
}
