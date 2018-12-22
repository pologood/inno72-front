package com.inno72.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.datetime.LocalDateUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72MerchantMapper;
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
	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;

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
//			Inno72MerchantTotalCountVo vo = inno72MerchantTotalCountMapper.selectMaxMinTime(activityId);
//			BeanUtils.copyProperties(vo, countVo);
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
			countVo.setMachineInfo(addressNums);
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
		long rgoods = 0L;

		if (allById.getData().size() > 0) {
			for (Inno72MerchantTotalCount count : allById.getData()) {
				rorder += count.getOrder();
				rbuyer += count.getBuyer();
				ruv += count.getUv();
				rpv += count.getPv();
				rvs += count.getVisitorNum();
				rgoods += count.getShipment();
			}
		}
		Map<String, Long> totle = new HashMap<>(5);

		totle.put("rvs", rvs);
		totle.put("rpv", rpv);
		totle.put("ruv", ruv);
		totle.put("ros", rorder);
		totle.put("rbs", rbuyer);
		totle.put("rgoods", rgoods);

		return Results.success(totle);
	}

	@Override
	public Result<List<Map<String, Object>>> addressNum(String actId) {
		List<Map<String, Object>> addressNums = new ArrayList<>();
		Map<String, Object> addressNum = new HashMap<>();
			addressNum.put("address", "北京");
			addressNum.put("num", 15);
			addressNums.add(addressNum);
			addressNum = new HashMap<>();
			addressNum.put("address", "杭州");
			addressNum.put("num", 7);
			addressNums.add(addressNum);
			addressNum = new HashMap<>();
			addressNum.put("address", "上海");
			addressNum.put("num", 8);
			addressNums.add(addressNum);
		return Results.success(addressNums);
	}

	@Override
	public Result<List<ActMerchantLog>> actLog(String actId) {

		List<ActMerchantLog> actMerchantLogs = new ArrayList<>();


			//点72 活动
		if (actId.equals("03e0c821671a4d6f8fad0d47fa25f040")){
			ActMerchantLog log = new ActMerchantLog();
			log.setId(StringUtil.uuid());
			log.setTime("2018-12-15");
			log.setCount("30");
			log.setType("新增");
			actMerchantLogs.add(log);
			// 备选活动
		}else if (actId.equals("40e48662e73340a496e117653edd2ef5")){
			ActMerchantLog log = new ActMerchantLog();
			log.setId(StringUtil.uuid());
			log.setTime("2018-12-13");
			log.setCount("217");
			log.setType("新增");
			actMerchantLogs.add(log);
		}
		return Results.success(actMerchantLogs);
	}

	/**
	 *
	 * @param actId
	 * @return startTime
	 * 		   endTime
	 * 		   status
	 * 		   totalTime
	 */
	@Override
	public Result<Inno72MerchantTotalCountVo> actInfo(String actId, String merchantId) {
		if (StringUtil.isEmpty(actId) || StringUtil.isEmpty(merchantId)){
			return Results.failure("请求错误!");
		}

		if (actId.equals("03e0c821671a4d6f8fad0d47fa25f040")){
			return defaultActInfo();
		}else if (actId.equals("40e48662e73340a496e117653edd2ef5")){
			return defaultActBeiXuanInfo();
		}
		Inno72MerchantUser user = inno72MerchantUserMapper.selectByPrimaryKey(merchantId);
		if (user == null){
			return Results.failure("没有这个商户商户!");
		}

		List<String> list = inno72MerchantMapper.selectMerchantId(user.getId());
		if (list.size() == 0){
			return Results.failure("商户没有任何的商家!");
		}

		Map<String, Object> param = new HashMap<>();
		param.put("activityId", actId);
		param.put("merchantId", user.getMerchantId());
		param.put("list", list);

		Inno72MerchantTotalCount count = inno72MerchantTotalCountMapper.selectByActIdAndMerId(param);
		if (count == null){
			return Results.failure("商户下没有这个活动!");
		}

		Inno72MerchantTotalCountVo countVo = inno72MerchantTotalCountMapper.selectMaxMinTime(param);
		if (countVo == null){
			return Results.failure("没有这个活动的配置!");
		}
		String startTime = countVo.getStartTime();
		String endTime = countVo.getEndTime();

		if (StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime)){

			LocalDateTime startLocalTime = LocalDateTimeUtil.transfer(startTime);
			LocalDateTime endLocalTime = LocalDateTimeUtil.transfer(endTime);


			String status = "";

			LocalDate localDateStart = startLocalTime.toLocalDate();
			LocalDate localDateEnd = endLocalTime.toLocalDate();
			LocalDate now = LocalDate.now();
			boolean before = localDateStart.isBefore(now);
			boolean before1 = now.isBefore(localDateEnd);
			if (before && before1){
				status = "1";
			}
			if (!before){
				status = "2";
			}
			if (!before1){
				status = "0";
			}
			long totalTime = 0;
			if (status.equals("1")){
				totalTime = Duration.between(startLocalTime, now).toHours();
			}else {
				totalTime = Duration.between(startLocalTime, endLocalTime).toHours();
			}

			countVo.setTotalTime(totalTime+"");
			countVo.setActivityStatus(status);
			countVo.setStartTime(localDateStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			countVo.setEndTime(localDateEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			countVo.setGoodsNum(count.getShipment());
			countVo.setPv(count.getPv());

		}

		return Results.success(countVo);
	}

	private Result<Inno72MerchantTotalCountVo> defaultActInfo(){
		String startDate = "2018-12-15";
		String endTime = "2018-12-31";
		String status = "1";

		Duration between = Duration
				.between(LocalDateUtil.transfer(startDate), LocalDate.now());
		String totalTime = between.toHours()+"";

		Inno72MerchantTotalCountVo vo = new Inno72MerchantTotalCountVo();
		vo.setStartTime(startDate);
		vo.setEndTime(endTime);
		vo.setActivityStatus(status);
		vo.setTotalTime(totalTime);
		vo.setGoodsNum(53689);
		vo.setPv(69675);
		return Results.success(vo);
	}

	private Result<Inno72MerchantTotalCountVo> defaultActBeiXuanInfo(){
		String startDate = "2018-11-13";
		String endTime = "2018-12-20";
		String status = "0";

		Duration between = Duration
				.between(LocalDateUtil.transfer(startDate), LocalDate.now());
		String totalTime = between.toHours()+"";

		Inno72MerchantTotalCountVo vo = new Inno72MerchantTotalCountVo();
		vo.setStartTime(startDate);
		vo.setEndTime(endTime);
		vo.setActivityStatus(status);
		vo.setTotalTime(totalTime);
		vo.setGoodsNum(53689);
		vo.setPv(69675);
		return Results.success(vo);
	}
}
