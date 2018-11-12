package com.inno72.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
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
	@Resource
	private Inno72MerchantTotalCountByDayMapper inno72MerchantTotalCountByDayMapper;

	@Override
	public Result<Object> searchData(String label, String activityId, String city, String startDate, String endDate) {

		List<Inno72MerchantTotalCountByDay> days = inno72MerchantTotalCountByDayMapper.selectList(activityId, city, startDate, endDate);
		Map<String, Object> result = new HashMap<>(2);
		switch (label){
			case "order":
				result = this.buildOrder(days);
				break;
			case "goods":
				result = this.buildGoods(days);
				break;
			case "user":
				result = this.buildUser(days);
				break;
			default:
				return Results.failure("无查询类型!");
		}
		return Results.success(result);
	}

	private Map<String, Object> buildOrder(List<Inno72MerchantTotalCountByDay> days) {
		Map<String, Object> result = new HashMap<>(2);
		//chart list
		result.put("list", days);

		days.sort((arg0, arg1) -> arg0.getDate().compareTo(arg1.getDate()));

		Map<String, List<String>> chart = new HashMap<>(2);
		List<String> x = new ArrayList<>(); // 日期
		Map<String, Object> y = new HashMap<>(6);
		List<Integer> orderQtyTotalS = new ArrayList<>(); // 数量
		List<Integer> orderQtySuccS = new ArrayList<>(); // 数量
		List<Integer> goodsNumS = new ArrayList<>(); // 数量
		List<Integer> uvS = new ArrayList<>(); // 数量
		List<Integer> pvS = new ArrayList<>(); // 数量
		List<Integer> couponNumS = new ArrayList<>(); // 数量

		Map<String, Object> map = new HashMap<>();
		for (Inno72MerchantTotalCountByDay day : days){

			x.add(day.getDate());

			orderQtyTotalS.add(day.getOrderQtyTotal());
			orderQtySuccS.add(day.getOrderQtySucc());
			goodsNumS.add(day.getGoodsNum());
			uvS.add(day.getUv());
			pvS.add(day.getPv());
			couponNumS.add(day.getCouponNum());

		}

		y.put("orderQtyTotalS", orderQtyTotalS);
		y.put("orderQtySuccS", orderQtySuccS);
		y.put("goodsNumS", goodsNumS);
		y.put("uvS", uvS);
		y.put("pvS", pvS);
		y.put("couponNumS", couponNumS);

		map.put("x", x);
		map.put("y", y);

		result.put("chart", map);

		return result;
	}

	private Map<String, Object> buildUser(List<Inno72MerchantTotalCountByDay> days) {
		Map<String, Object> result = new HashMap<>(2);
		Map<String, List<Inno72MerchantTotalCountByDay>> map = new HashMap<>();
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

		list.sort((o1, o2) -> o1.get("date").compareTo(o2.get("date")));

		List<String> x = new ArrayList<>();

		List<String> experienceS = new ArrayList<>();
		List<String> concernS = new ArrayList<>();
		for (Map<String, String> stringMap : list){

			String date = stringMap.get("date");
			x.add(date);
			String experience = stringMap.get("experience");
			experienceS.add(experience);
			String concern = stringMap.get("concern");
			concernS.add(concern);


		}
		Map<String,List<String>> ys = new HashMap<>();
		ys.put("experienceS", experienceS);
		ys.put("concernS", concernS);

		Map<String, Object> aa = new HashMap<>();
		aa.put("x", x);
		aa.put("y", ys);
		result.put("chart", aa);

		return result;
	}

	private Map<String, Object> buildGoods(List<Inno72MerchantTotalCountByDay> days) {
		Map<String, Object> result = new HashMap<>(2);
		Map<String, List<Inno72MerchantTotalCountByDay>> map = new HashMap<>();
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

		List<Map<String, Object>> list = new ArrayList<>();

		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> entry : map.entrySet()){
			List<Inno72MerchantTotalCountByDay> value = entry.getValue();

			int totlePv = 0;
			int totleUv = 0;
			String city = value.get(0).getCity();
			String date = value.get(0).getDate();

			Map<String, Object> goodsMap = new HashMap<>();

			for (Inno72MerchantTotalCountByDay day : value){
				totlePv += day.getPv();
				totleUv += day.getUv();
				String goodsId = day.getGoodsId();

				Map<String, String> goods = Optional.ofNullable(goodsMap.get(goodsId)).map((v)-> (Map<String, String>) v).orElse(new HashMap<>());
				String goodsNum = goods.get("goodsNum");
				if (StringUtil.isEmpty(goodsNum)){
					goods.put("goodsNum", day.getGoodsNum()+"");
				}else {
					goods.put("goodsNum", (day.getGoodsNum() + Integer.parseInt(goodsNum))+"");
				}
				String goodsName = goods.get("goodsName");
				if (StringUtil.isEmpty(goodsName)){
					goods.put("goodsName", goodsName);
				}
				if (StringUtil.isEmpty(goodsId)){
					goods.put("goodsId", goodsId);
				}
				goodsMap.put(goodsId, goods);

			}
			goodsMap.put("pv", totlePv);
			goodsMap.put("uv", totleUv);
			list.add(goodsMap);
		}

		result.put("list", list);

		List<Object> x = new ArrayList<>();
		List<Object> experienceS = new ArrayList<>();
		List<Object> concernS = new ArrayList<>();
		for (Map<String, Object> stringMap : list){

			Object date = stringMap.get("date");
			x.add(date);
			Object experience = stringMap.get("experience");
			experienceS.add(experience);
			Object concern = stringMap.get("concern");
			concernS.add(concern);


		}
		Map<String, List<Object>> ys = new HashMap<>();
		ys.put("experienceS", experienceS);
		ys.put("concernS", concernS);

		Map<String, Object> aa = new HashMap<>();
		aa.put("x", x);
		aa.put("y", ys);
		result.put("chart", aa);

		return result;
	}
}
