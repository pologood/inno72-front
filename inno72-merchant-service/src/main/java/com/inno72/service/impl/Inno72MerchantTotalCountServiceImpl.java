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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72ActivityIndexMapper;
import com.inno72.mapper.Inno72ActivityInfoDescMapper;
import com.inno72.mapper.Inno72MerchantMapper;
import com.inno72.mapper.Inno72MerchantTotalCountMapper;
import com.inno72.mapper.Inno72MerchantUserMapper;
import com.inno72.model.Inno72ActivityIndex;
import com.inno72.model.Inno72ActivityInfoDesc;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72MerchantTotalCountServiceImpl.class);

	@Resource
	private Inno72MerchantTotalCountMapper inno72MerchantTotalCountMapper;
	@Resource
	private Inno72MerchantUserMapper inno72MerchantUserMapper;
	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;
	@Resource
	private Inno72ActivityInfoDescMapper inno72ActivityInfoDescMapper;
	@Resource
	private Inno72ActivityIndexMapper inno72ActivityIndexMapper;

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
		LocalDateTime now = LocalDateTime.now();
		for (Inno72MerchantTotalCountVo countVo : inno72MerchantTotalCounts) {
			String activityId = countVo.getActivityId();
			List<String> list = inno72MerchantMapper.selectMerchantId(user.getId());
			if (list.size() == 0) {
				return Results.failure("商户没有任何的商家!");
			}

			Map<String, Object> param = new HashMap<>();
			param.put("activityId", activityId);
			param.put("list", list);

			Inno72MerchantTotalCountVo vo = inno72MerchantTotalCountMapper.selectMaxMinTime(param);

			if (vo != null && StringUtil.notEmpty(vo.getStartTime()) && StringUtil.notEmpty(vo.getEndTime())) {

				countVo.setStartTime(vo.getStartTime());
				countVo.setEndTime(vo.getEndTime());

				LocalDateTime parseEnd = LocalDateTime.parse(vo.getEndTime(),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				LocalDateTime parseStart = LocalDateTime.parse(vo.getStartTime(),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				Duration between;
				if (now.isAfter(parseEnd)) {
					between = Duration.between(parseStart, parseEnd);
				} else {
					between = Duration.between(parseStart, now);
				}
				countVo.setTotalTime(between.toHours() + "");
			}

			List<Map<String, Object>> maps = inno72MerchantTotalCountMapper.selectMachineNumCity(param);
			countVo.setMachineInfo(maps);

			// 点72 活动
			if (activityId.equals("03e0c821671a4d6f8fad0d47fa25f040")) {

				String startDate = "2018-12-15 00:00:00";
				String endTime = "2018-12-31 23:59:59";

				countVo.setStartTime("2018-12-15");
				countVo.setEndTime("2018-12-31");
				Duration between;
				LocalDateTime parseEnd = LocalDateTime.parse(endTime,
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				LocalDateTime parseStart = LocalDateTime.parse(startDate,
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				if (now.isAfter(parseEnd)) {
					between = Duration.between(parseStart, parseEnd);
				} else {
					between = Duration.between(parseStart, now);
				}
				String totalTime = between.toHours() + "";
				countVo.setTotalTime(totalTime);

				List<Map<String, Object>> addressNums = new ArrayList<>();
				Map<String, Object> addressNum = new HashMap<>();
				addressNum.put("address", "北京");
				addressNum.put("num", 15);
				addressNums.add(addressNum);
				addressNum = new HashMap<>();
				addressNum.put("address", "上海");
				addressNum.put("num", 8);
				addressNums.add(addressNum);
				addressNum = new HashMap<>();
				addressNum.put("address", "杭州");
				addressNum.put("num", 7);
				addressNums.add(addressNum);
				countVo.setMachineInfo(addressNums);


				// 备选活动
			} else if (activityId.equals("40e48662e73340a496e117653edd2ef5")) {

				String startDate = "2018-11-13 00:00:00";
				String endTime = "2018-12-20 23:59:59";

				countVo.setStartTime("2018-11-13");
				countVo.setEndTime("2018-12-20");
				Duration between;
				LocalDateTime parseEnd = LocalDateTime.parse(endTime,
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				LocalDateTime parseStart = LocalDateTime.parse(startDate,
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				if (now.isAfter(parseEnd)) {
					between = Duration.between(parseStart, parseEnd);
				} else {
					between = Duration.between(parseStart, now);
				}
				String totalTime = between.toHours() + "";
				countVo.setTotalTime(totalTime);

				List<Map<String, Object>> addressNums = new ArrayList<>();
				Map<String, Object> addressNum = new HashMap<>();
				addressNum.put("address", "北京市");
				addressNum.put("num", 40);
				addressNums.add(addressNum);
				addressNum = new HashMap<>();
				addressNum.put("address", "杭州市");
				addressNum.put("num", 15);
				addressNums.add(addressNum);
				addressNum = new HashMap<>();
				addressNum.put("address", "南京市");
				addressNum.put("num", 59);
				addressNums.add(addressNum);
				addressNum = new HashMap<>();
				addressNum.put("address", "广州市");
				addressNum.put("num", 23);
				addressNums.add(addressNum);
				addressNum = new HashMap<>();
				addressNum.put("address", "成都市");
				addressNum.put("num", 41);
				addressNums.add(addressNum);
				addressNum = new HashMap<>();
				addressNum.put("address", "深圳市");
				addressNum.put("num", 31);
				addressNums.add(addressNum);
				addressNum = new HashMap<>();
				addressNum.put("address", "重庆市");
				addressNum.put("num", 44);
				addressNums.add(addressNum);
				addressNum = new HashMap<>();
				addressNum.put("address", "上海市");
				addressNum.put("num", 13);
				addressNums.add(addressNum);
				countVo.setMachineInfo(addressNums);
			}

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
		if (actId.equals("03e0c821671a4d6f8fad0d47fa25f040")) {
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
			// 备选活动
		} else if (actId.equals("40e48662e73340a496e117653edd2ef5")) {
			Map<String, Object> addressNum = new HashMap<>();
			addressNum.put("address", "北京市");
			addressNum.put("num", 15);
			addressNums.add(addressNum);
			addressNum = new HashMap<>();
			addressNum.put("address", "杭州市");
			addressNum.put("num", 7);
			addressNums.add(addressNum);
			addressNum.put("address", "南京市");
			addressNum.put("num", 7);
			addressNums.add(addressNum);
			addressNum.put("address", "广州市");
			addressNum.put("num", 7);
			addressNums.add(addressNum);
			addressNum.put("address", "成都市");
			addressNum.put("num", 7);
			addressNums.add(addressNum);
			addressNum.put("address", "深圳市");
			addressNum.put("num", 7);
			addressNums.add(addressNum);
			addressNum.put("address", "重庆市");
			addressNum.put("num", 7);
			addressNums.add(addressNum);
			addressNum = new HashMap<>();
			addressNum.put("address", "上海市");
			addressNum.put("num", 8);
			addressNums.add(addressNum);
		}

		return Results.success(addressNums);
	}

	@Override
	public Result<List<ActMerchantLog>> actLog(String actId, String merchantId) {

		Map<String, String> param = new HashMap<>();
		param.put("actId", actId);
		param.put("merchantId", merchantId);
		List<Inno72ActivityInfoDesc> descS = inno72ActivityInfoDescMapper.selectActInfoDesc(param);

		List<ActMerchantLog> actMerchantLogs = new ArrayList<>();
		ActMerchantLog log;

		for (Inno72ActivityInfoDesc desc : descS){

			LocalDate infoDate = desc.getInfoDate();
			String infoDesc = desc.getInfoDesc();
			Integer infoType = desc.getInfoType();

			log = new ActMerchantLog();
			log.setId(StringUtil.uuid());
			log.setTime(infoDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			log.setCount(infoDesc);
			log.setType(infoType == 1 ? "新增" : "其他");

			actMerchantLogs.add(log);

		}

		// 点72 活动
		if (actId.equals("03e0c821671a4d6f8fad0d47fa25f040")) {
			log = new ActMerchantLog();
			log.setId(StringUtil.uuid());
			log.setTime("2018-12-15");
			log.setCount("新增 30 台机器");
			log.setType("新增");
			actMerchantLogs.add(log);
			// 备选活动
		} else if (actId.equals("40e48662e73340a496e117653edd2ef5")) {
			log = new ActMerchantLog();
			log.setId(StringUtil.uuid());
			log.setTime("2018-12-13");
			log.setCount("新增 265 台机器");
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
		if (StringUtil.isEmpty(actId) || StringUtil.isEmpty(merchantId)) {
			return Results.failure("请求错误!");
		}

		if (actId.equals("03e0c821671a4d6f8fad0d47fa25f040")) {
			return defaultActInfo();
		} else if (actId.equals("40e48662e73340a496e117653edd2ef5")) {
			return defaultActBeiXuanInfo();
		}
		Inno72MerchantUser user = inno72MerchantUserMapper.selectByMerchantId(merchantId);
		if (user == null) {
			return Results.failure("没有这个商户商户!");
		}

		List<String> list = inno72MerchantMapper.selectMerchantId(user.getId());
		if (list.size() == 0) {
			return Results.failure("商户没有任何的商家!");
		}

		Map<String, Object> param = new HashMap<>();
		param.put("activityId", actId);
		param.put("merchantId", user.getMerchantId());
		param.put("list", list);

		Inno72MerchantTotalCount count = inno72MerchantTotalCountMapper.selectByActIdAndMerId(param);
		if (count == null) {
			return Results.failure("商户下没有这个活动!");
		}

		Inno72MerchantTotalCountVo countVo = inno72MerchantTotalCountMapper.selectMaxMinTime(param);
		if (countVo == null) {
			return Results.failure("没有这个活动的配置!");
		}

		List<Inno72ActivityIndex> indexList = inno72ActivityIndexMapper.selectIndex(param);

		countVo.setIndexList(indexList);

		countVo.setActivityName(count.getActivityName());
		countVo.setActivityId(actId);
		countVo.setMerchantId(user.getMerchantId());
		countVo.setPv(count.getPv());
		countVo.setUv(count.getUv());
		countVo.setShipment(count.getShipment());
		countVo.setOrder(count.getOrder());
		countVo.setGoodsNum(count.getShipment());

		String startTime = countVo.getStartTime();
		String endTime = countVo.getEndTime();

		if (StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime)) {

			LOGGER.info("查询活动{},和商家{},的用户下{} 活动开始结束时间", actId, JSON.toJSONString(user), JSON.toJSONString(list),
					JSON.toJSONString(countVo));

			LocalDateTime startLocalTime = LocalDateTimeUtil.transfer(startTime,
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime endLocalTime = LocalDateTimeUtil.transfer(endTime,
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


			String status = "";

			LocalDateTime now = LocalDateTime.now();
			boolean before = startLocalTime.isBefore(now);
			boolean before1 = now.isBefore(endLocalTime);
			if (before && before1) {
				status = "1";
			}
			if (!before) {
				status = "2";
			}
			if (!before1) {
				status = "0";
			}
			long totalTime = 0;
			if (status.equals("1")) {
				totalTime = Duration.between(startLocalTime, now).toHours();
			} else {
				totalTime = Duration.between(startLocalTime, endLocalTime).toHours();
			}

			countVo.setTotalTime(totalTime + "");
			countVo.setActivityStatus(status);
			countVo.setStartTime(startLocalTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			countVo.setEndTime(endLocalTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

		}

		return Results.success(countVo);
	}

	private Result<Inno72MerchantTotalCountVo> defaultActInfo() {
		String startDate = "2018-12-15 00:00:00";
		String endTime = "2018-12-31 23:59:59";
		String status = "0";

		Duration between;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime parseEnd = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDateTime parseStart = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		if (now.isAfter(parseEnd)) {
			between = Duration.between(parseStart, parseEnd);
		} else {
			between = Duration.between(parseStart, now);
		}
		String totalTime = between.toHours() + "";

		Inno72MerchantTotalCountVo vo = new Inno72MerchantTotalCountVo();
		vo.setStartTime(startDate.substring(0, 10));
		vo.setEndTime(endTime.substring(0, 10));
		vo.setActivityStatus(status);
		vo.setTotalTime(totalTime);
		vo.setGoodsNum(53689);
		vo.setPv(69675);
		vo.setActivityName("点七二互动活动");
		System.out.println("默认活动返回-》" + JSON.toJSONString(vo));
		return Results.success(vo);
	}

	private Result<Inno72MerchantTotalCountVo> defaultActBeiXuanInfo() {
		String startDate = "2018-11-13 00:00:00";
		String endTime = "2018-12-20 23:59:59";
		String status = "0";

		Duration between = Duration.between(
				LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		String totalTime = between.toHours() + "";

		Inno72MerchantTotalCountVo vo = new Inno72MerchantTotalCountVo();
		vo.setActivityName("新芝华士备选活动");
		vo.setStartTime(startDate.substring(0, 10));
		vo.setEndTime(endTime.substring(0, 10));
		vo.setActivityStatus(status);
		vo.setTotalTime(totalTime);
		vo.setGoodsNum(53689);
		vo.setPv(69675);
		System.out.println("新芝华士备选活动返回-》" + JSON.toJSONString(vo));
		return Results.success(vo);
	}
}
