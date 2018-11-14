package com.inno72.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.datetime.LocalDateUtil;
import com.inno72.common.utils.StringUtil;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72MerchantTotalCountByDayServiceImpl.class);
	@Resource
	private Inno72MerchantTotalCountByDayMapper inno72MerchantTotalCountByDayMapper;

	@Override
	public Result<Object> searchData(String label, String activityId, String city, String startDate, String endDate,
			String goods) {

		if (StringUtil.isEmpty(activityId) && StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)){
			return Results.failure("参数缺失!");
		}
		LOGGER.info("查询列表 -> label - {}, activityId - {}, city - {}, startDate - {}, endDate - {}, goods - {}"
		,label, activityId, city, startDate, endDate, goods);
		LocalDate startDateLocal = LocalDateUtil.transfer(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate endDateLocal = LocalDateUtil.transfer(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		if (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear() > 90){
			return Results.failure("不能大于三个月!");
		}

		List<Inno72MerchantTotalCountByDay> days = inno72MerchantTotalCountByDayMapper.selectList(activityId, city, startDate, endDate, goods);
		Map<String, Object> result;
		switch (label){
			case "order":
				result = this.buildOrder(days, startDateLocal, endDateLocal);
				break;
			case "goods":
				result = this.buildGoods(days, startDateLocal, endDateLocal);
				break;
			case "user":
				result = this.buildUser(days, startDateLocal, endDateLocal);
				break;
			default:
				return Results.failure("无查询类型!");
		}
		return Results.success(result);
	}

	private Map<String, Object> buildOrder(List<Inno72MerchantTotalCountByDay> days, LocalDate startDateLocal, LocalDate endDateLocal) {
		Map<String, Object> result = new HashMap<>(2);
		//chart list
		result.put("list", days);

		days.sort(Comparator.comparing(Inno72MerchantTotalCountByDay::getDate));

		//处理charts部分数据

		//按日期分组所有数据 key = 日期 ； value 分组的数据
		Map<String, List<Inno72MerchantTotalCountByDay>> kk = kk(days, startDateLocal, endDateLocal);

		LocalDate thisDate = startDateLocal;

		Map<String, Object> y = new HashMap<>(6);
		List<Integer> orderQtyTotalS = new ArrayList<>(); // 数量
		List<Integer> orderQtySuccS = new ArrayList<>(); // 数量
		List<Integer> goodsNumS = new ArrayList<>(); // 数量
		List<Integer> uvS = new ArrayList<>(); // 数量
		List<Integer> pvS = new ArrayList<>(); // 数量
		List<Integer> couponNumS = new ArrayList<>(); // 数量

		//统计单日下所有数量的总和
		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> k : kk.entrySet()){

			if (StringUtil.isEmpty(k.getKey())){
				continue;
			}

			List<Inno72MerchantTotalCountByDay> value = k.getValue();
			int orderQtyTotal = 0;
			int goodsNum = 0;
			int pv = 0;
			int couponNum = 0;
			int uv = 0;
			int orderQtySucc = 0;
			for (Inno72MerchantTotalCountByDay day:value){

				goodsNum += day.getGoodsNum();
				orderQtyTotal += day.getOrderQtyTotal();
				orderQtySucc += day.getOrderQtySucc();
				pv += day.getPv();
				uv += day.getUv();
				couponNum += day.getCouponNum();
			}

			LocalDate cDate = LocalDateUtil.transfer(k.getKey(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			//计算日差，如果当前日期数据小于传入的开始日期。补充0的数据到数组
			while (thisDate.plusDays(1).isBefore(cDate)) {
				orderQtyTotalS.add(0);
				orderQtySuccS.add(0);
				goodsNumS.add(0);
				uvS.add(0);
				pvS.add(0);
				couponNumS.add(0);
				thisDate = thisDate.plusDays(1);
			}
			//指针日期加一天
			thisDate = thisDate.plusDays(1);

			//归类数据
			orderQtyTotalS.add(orderQtyTotal);
			orderQtySuccS.add(orderQtySucc);
			goodsNumS.add(goodsNum);
			uvS.add(uv);
			pvS.add(pv);
			couponNumS.add(couponNum);

		}

		// 日期不足，补充0直到到结束日期
		while (orderQtyTotalS.size() < (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear())){
			orderQtyTotalS.add(0);
			orderQtySuccS.add(0);
			goodsNumS.add(0);
			uvS.add(0);
			pvS.add(0);
			couponNumS.add(0);
		}

		y.put("orderQtyTotalS", orderQtyTotalS);
		y.put("orderQtySuccS", orderQtySuccS);
		y.put("goodsNumS", goodsNumS);
		y.put("uvS", uvS);
		y.put("pvS", pvS);
		y.put("couponNumS", couponNumS);

		result.put("chart", y);

		return result;
	}

	private Map<String, List<Inno72MerchantTotalCountByDay>> kk(List<Inno72MerchantTotalCountByDay> days, LocalDate startDate, LocalDate endDate){
		days.sort(Comparator.comparing(Inno72MerchantTotalCountByDay::getDate));
		Map<String, List<Inno72MerchantTotalCountByDay>> map = new TreeMap<>();
		for (Inno72MerchantTotalCountByDay day : days){
			String date = day.getDate();
			List<Inno72MerchantTotalCountByDay> byDays = map.get(date);
			if (byDays == null){
				byDays = new ArrayList<>();
			}
			byDays.add(day);
			map.put(date, byDays);
		}
		return map;
	}


	private Map<String, Object> buildUser(List<Inno72MerchantTotalCountByDay> days, LocalDate startDateLocal, LocalDate endDateLocal) {
		Map<String, Object> result = new HashMap<>(2);
		Map<String, List<Inno72MerchantTotalCountByDay>> map = new HashMap<>();
		this.groupByCityAndDate(days, map);
		LOGGER.info("分组后的map -> {}", JSON.toJSONString(map));
		List<Map<String, String>> list = new ArrayList<>();
		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> entry : map.entrySet()){
			List<Inno72MerchantTotalCountByDay> value = entry.getValue();

			int totleStay = 0;
			int totleConcernNum = 0;
			String city = value.get(0).getCity();
			String date = value.get(0).getDate();

			for (Inno72MerchantTotalCountByDay day : value){
				totleStay += day.getStayNum();
				totleConcernNum += day.getConcernNum();
			}
			Map<String, String> userResult = new HashMap<>(4);
			userResult.put("date", date);
			userResult.put("city", city);
			userResult.put("experience", totleStay+"");
			userResult.put("concern", totleConcernNum+"");
			if (totleConcernNum != 0) {
				userResult.put("percent", totleStay / totleConcernNum * 100 + "");
			}else {
				userResult.put("percent", "0");
			}
			list.add(userResult);
		}

		result.put("list", list);

		Map<String, List<Map<String, String>>> kk = kku(list, startDateLocal, endDateLocal);

		List<Integer> experienceS = new ArrayList<>();
		List<Integer> concernS = new ArrayList<>();
		List<Integer> percentS = new ArrayList<>();

		LocalDate thisDate = startDateLocal;
		//统计单日下所有数量的总和
		for (Map.Entry k : kk.entrySet()){

			if (StringUtil.isEmpty(k.getKey())){
				continue;
			}
			List<Map<String, String>> value = (List<Map<String, String>>) k.getValue();

			int experience = 0;
			int concern = 0;
			int percent = 0;

			for (Map<String, String> day:value){

				experience += Integer.parseInt(day.get("experience"));
				concern += Integer.parseInt(day.get("concern"));
				percent += Integer.parseInt(day.get("percent"));
			}
			LocalDate cDate = LocalDateUtil.transfer((String) k.getKey(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			//计算日差，如果当前日期数据小于传入的开始日期。补充0的数据到数组
			while (thisDate.plusDays(1).isBefore(cDate)) {
				experienceS.add(0);
				concernS.add(0);
				percentS.add(0);
				thisDate = thisDate.plusDays(1);
			}
			//指针日期加一天
			thisDate = thisDate.plusDays(1);

			//归类数据
			experienceS.add(experience);
			concernS.add(concern);
			percentS.add(percent);

		}

		// 日期不足，补充0直到到结束日期
		while (experienceS.size() < (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear())){
			experienceS.add(0);
			concernS.add(0);
			percentS.add(0);
		}
//
//		for (Map<String, String> stringMap : list){
//
//			String date = stringMap.get("date");
//			LocalDate thisDate = LocalDateUtil.transfer(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//			while (startDateLocal.plusDays(1).isBefore(thisDate)){
//				startDateLocal = startDateLocal.plusDays(1);
//				experienceS.add(0);
//				concernS.add(0);
//				LOGGER.info("构建订单数据 日期 {}",
//						startDateLocal);
//			}
//			startDateLocal = startDateLocal.plusDays(1);
//
//			experienceS.add(Integer.parseInt(stringMap.get("experience")));
//
//			concernS.add(Integer.parseInt(stringMap.get("concern")));
//
//			percentS.add(Integer.parseInt(stringMap.get("percent")));
//
//		}
//
//		// 日期不足，补充0直到到结束日期
//		while (experienceS.size() < (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear())){
//			experienceS.add(0);
//			concernS.add(0);
//			percentS.add(0);
//		}

		Map<String,List<Integer>> ys = new HashMap<>();
		ys.put("experienceS", experienceS);
		ys.put("percentS", percentS);
		ys.put("concernS", concernS);
		result.put("chart", ys);

		return result;
	}

	private Map<String,List<Map<String,String>>> kku(List<Map<String,String>> list, LocalDate startDateLocal, LocalDate endDateLocal) {
		list.sort(Comparator.comparing( (v) -> v.get("date")));

		Map<String, List<Map<String,String>>> map = new TreeMap<>();
		for (Map<String,String> day : list){
			String date = day.get("date");
			List<Map<String,String>> byDays = map.get(date);
			if (byDays == null){
				byDays = new ArrayList<>();
			}
			byDays.add(day);
			map.put(date, byDays);
		}
		return map;
	}

	private void groupByCityAndDate(List<Inno72MerchantTotalCountByDay> days,
			Map<String, List<Inno72MerchantTotalCountByDay>> map) {
		for (Inno72MerchantTotalCountByDay day : days){
			String city = day.getCity();
			String date = day.getDate();

			String key = city + date;
			List<Inno72MerchantTotalCountByDay> value = map.get(key);
			if (value == null){
				value = new ArrayList<>();
			}
			value.add(day);
			map.put(key, value);
		}
	}

	private Map<String, Object> buildGoods(List<Inno72MerchantTotalCountByDay> days, LocalDate startDateLocal, LocalDate endDateLocal) {
		Map<String, Object> result = new HashMap<>(2);
		result.put("list", days);

		Map<String, List<Inno72MerchantTotalCountByDay>> map = new HashMap<>();
		this.groupByCityAndDate(days, map);

		List<Inno72MerchantTotalCountByDay> resultList = new ArrayList<>();
		List<Map<String, String>> pvuvMap = new ArrayList<>();
		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> entry : map.entrySet()){
			List<Inno72MerchantTotalCountByDay> value = entry.getValue();

			int totlePv = 0;
			int totleUv = 0;
			String date  = "";
			for (Inno72MerchantTotalCountByDay day : value){
				totlePv += day.getPv();
				totleUv += day.getUv();
				if (StringUtil.isEmpty(date) && StringUtil.notEmpty(day.getDate())){
					date = day.getDate();
				}
			}
			for (Inno72MerchantTotalCountByDay day : value){
				day.setPv(totlePv);
				day.setUv(totleUv);
			}
			Map<String, String> pvuv = new HashMap<>(2);
			pvuv.put("pv", totlePv+"");
			pvuv.put("uv", totleUv+"");
			pvuv.put("date", date);
			pvuvMap.add(pvuv);
			resultList.addAll(value);
		}
		result.put("list", resultList);
		pvuvMap.sort(Comparator.comparing(o -> o.get("date")));

		List<Integer> pvs = new ArrayList<>();
		List<Integer> uvs = new ArrayList<>();
		LocalDate addDateLocal = startDateLocal;
		for (Map<String, String> e : pvuvMap){
			String pv = e.get("pv");
			String uv = e.get("uv");
			String date = e.get("date");
			LocalDate thisDate = LocalDateUtil.transfer(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			while (addDateLocal.plusDays(1).isBefore(thisDate)){
				pvs.add(0);
				uvs.add(0);
				addDateLocal = addDateLocal.plusDays(1);
				LOGGER.info("商品维度 日期 - {}, uvs - {}, pvs - {}", addDateLocal, uvs, pvs);
			}
			pvs.add(Integer.parseInt(pv));
			uvs.add(Integer.parseInt(uv));
			addDateLocal = addDateLocal.plusDays(1);
		}
		while (pvs.size() < (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear())){
			pvs.add(0);
			uvs.add(0);
		}


		Map<String, List<Inno72MerchantTotalCountByDay>> groupByGoodsId = new HashMap<>();
		for (Inno72MerchantTotalCountByDay day : days){
			String goodsId = day.getGoodsId();
			List<Inno72MerchantTotalCountByDay> byDays = groupByGoodsId.get(goodsId);
			if (byDays == null){
				byDays = new ArrayList<>();
			}
			byDays.add(day);
			groupByGoodsId.put(goodsId, byDays);
		}

		List<List<Integer>> nums = new ArrayList<>();
		List<Map<String, String>> names = new ArrayList<>();
		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> entry : groupByGoodsId.entrySet()){
			List<Inno72MerchantTotalCountByDay> value = entry.getValue();

			value.sort(Comparator.comparing(Inno72MerchantTotalCountByDay::getDate));
			Map<String, String> map1 = new TreeMap<>();
			String goodsId = value.get(0).getGoodsId();
			String goodsName = value.get(0).getGoodsName();
			map1.put("goodsName", goodsName);
			map1.put("goodsId", goodsId);
			names.add(map1);
			List<Integer> num = new ArrayList<>();

			LocalDate curLocalDate = startDateLocal;
			for (Inno72MerchantTotalCountByDay inno72MerchantTotalCountByDay : value){
				String date = inno72MerchantTotalCountByDay.getDate();
				LocalDate thisDate = LocalDateUtil.transfer(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				while (curLocalDate.plusDays(1).isBefore(thisDate)){
					num.add(0);
					curLocalDate = curLocalDate.plusDays(1);
					LOGGER.info("商品维度 日期 - {}, num - {}", curLocalDate, num);
				}

				num.add(inno72MerchantTotalCountByDay.getGoodsNum());
				curLocalDate = curLocalDate.plusDays(1);
				LOGGER.info("商品维度 日期 - {}, num - {}", curLocalDate, num);
			}

			// 日期不足，补充0直到到结束日期
			while (num.size() < (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear())){
				num.add(0);
			}
			nums.add(num);
		}

		Map<String, Object> charts = new HashMap<>();
		Map<String, String> map1 = new TreeMap<>();
		map1.put("goodsName", "互动次数");
		map1.put("goodsId", "");
		names.add(map1);
		Map<String, String> map2 = new TreeMap<>();
		map2.put("goodsName", "互动人数");
		map2.put("goodsId", "");
		names.add(map2);
		charts.put("name", names);
		nums.add(pvs);
		nums.add(uvs);
		charts.put("num", nums);
		result.put("charts", charts);
		return result;
	}
}
