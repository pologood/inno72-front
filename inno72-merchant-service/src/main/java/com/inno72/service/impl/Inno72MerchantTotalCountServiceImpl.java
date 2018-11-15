package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.Inno72MerchantTotalCountMapper;
import com.inno72.mapper.Inno72MerchantUserMapper;
import com.inno72.model.Inno72MerchantTotalCount;
import com.inno72.model.Inno72MerchantUser;
import com.inno72.service.Inno72MerchantTotalCountService;


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
	public Result<List<Inno72MerchantTotalCount>> findAllById(String id) {
		Inno72MerchantUser user = inno72MerchantUserMapper.selectByPrimaryKey(id);
		if ( user == null ){
			return Results.failure("无商户用户!");
		}
		String sellerId = user.getSellerId();
		if (StringUtils.isEmpty(sellerId)){
			return Results.failure("商户信息异常!");
		}
		List<Inno72MerchantTotalCount> inno72MerchantTotalCounts =
				inno72MerchantTotalCountMapper.selectBySellerId(sellerId);
		return Results.success(inno72MerchantTotalCounts);
	}

	@Override
	public Result<Object> totle(String id) {

		Result<List<Inno72MerchantTotalCount>> allById = findAllById(id);
		if (allById.getCode() == Result.FAILURE){
			return Results.failure(allById.getMsg());
		}

		long rvs = 0L;
		long rpv = 0L;
		long ruv = 0L;
		long rorder = 0L;
		long rbuyer = 0L;

		if (allById.getData().size() > 0){
			for (Inno72MerchantTotalCount count : allById.getData()){
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
}
