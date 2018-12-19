package com.inno72.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72MerchantTotalCountMapper;
import com.inno72.mapper.Inno72MerchantUserMapper;
import com.inno72.model.Inno72MerchantTotalCount;
import com.inno72.model.Inno72MerchantUser;
import com.inno72.service.Inno72MerchantTotalCountService;
import com.inno72.vo.ActMerchantLog;
import com.inno72.vo.Inno72MerchantTotalCountVo;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
@Service
@Transactional
public class Inno72MerchantTotalCountServiceImpl extends AbstractService<Inno72MerchantTotalCount>
		implements Inno72MerchantTotalCountService {
	@Resource
	private Inno72MerchantTotalCountMapper inno72MerchantTotalCountMapper;
	@Resource
	private Inno72MerchantUserMapper inno72MerchantUserMapper;

	@Override
	public Result<List<Inno72MerchantTotalCountVo>> findAllById(String id) {
		Inno72MerchantUser user = inno72MerchantUserMapper.selectByPrimaryKey(id);
		if (user == null) {
			return Results.failure("无商户用户!");
		}
		String merchantId = user.getMerchantId();
		if (StringUtils.isEmpty(merchantId)) {
			return Results.failure("商户信息异常!");
		}
		List<Inno72MerchantTotalCountVo> inno72MerchantTotalCounts = inno72MerchantTotalCountMapper
				.selectByMerchantId(merchantId);

		for (Inno72MerchantTotalCountVo countVo : inno72MerchantTotalCounts){
			String activityId = countVo.getActivityId();
			Inno72MerchantTotalCountVo vo = inno72MerchantTotalCountMapper.selectMaxMinTime(activityId);
			BeanUtils.copyProperties(vo, countVo);
		}
		return Results.success(inno72MerchantTotalCounts);
	}

	@Override
	public Result<Object> totle(String id) {

		Result<List<Inno72MerchantTotalCountVo>> allById = findAllById(id);
		if (allById.getCode() == Result.FAILURE) {
			return Results.failure(allById.getMsg());
		}

		long rvs = 0L;
		long rpv = 0L;
		long ruv = 0L;
		long rorder = 0L;
		long rbuyer = 0L;

		if (allById.getData().size() > 0) {
			for (Inno72MerchantTotalCount count : allById.getData()) {
				rorder += count.getOrder();
				rbuyer += count.getBuyer();
				ruv += count.getUv();
				rpv += count.getPv();
				rvs += count.getVisitorNum();
			}
		}
		Map<String, Long> totle = new HashMap<>(5);

		totle.put("rvs", rvs);
		totle.put("rpv", rpv);
		totle.put("ruv", ruv);
		totle.put("ros", rorder);
		totle.put("rbs", rbuyer);

		return Results.success(totle);
	}

	@Override
	public Result<List<Map<String, Object>>> addressNum(String actId) {
		List<Map<String, Object>> addressNums = new ArrayList<>();
		Map<String, Object> addressNum = new HashMap<>();
			addressNum.put("address", "北京");
			addressNum.put("num", 30);
			addressNums.add(addressNum);
			addressNum = new HashMap<>();
			addressNum.put("address", "南京");
			addressNum.put("num", 10);
			addressNums.add(addressNum);
			addressNum = new HashMap<>();
			addressNum.put("address", "上海");
			addressNum.put("num", 10);
			addressNums.add(addressNum);
		return Results.success(addressNums);
	}

	@Override
	public Result<List<ActMerchantLog>> actLog(String actId) {

		List<ActMerchantLog> actMerchantLogs = new ArrayList<>();
		ActMerchantLog log = new ActMerchantLog();
		log.setId(StringUtil.uuid());
		log.setTime("2018-12-01 10:41:24");
		log.setCount("新增50台机器");
		log.setType("新增");
		actMerchantLogs.add(log);
		return Results.success(actMerchantLogs);
	}
}
