package com.inno72.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72MerchantTotalCountByUserMapper;
import com.inno72.model.Inno72MerchantTotalCountByUser;
import com.inno72.service.Inno72MerchantTotalCountByUserService;


/**
 * Created by CodeGenerator on 2018/12/08.
 */
@Service
@Transactional
public class Inno72MerchantTotalCountByUserServiceImpl extends AbstractService<Inno72MerchantTotalCountByUser> implements Inno72MerchantTotalCountByUserService {
    @Resource
    private Inno72MerchantTotalCountByUserMapper inno72MerchantTotalCountByUserMapper;

	@Override
	public Result<Map<String, Object>> selectByActivityId(String activityId, String start, String end) {

		List<Inno72MerchantTotalCountByUser> users =
				inno72MerchantTotalCountByUserMapper.selectByActivityId(activityId, start, end);

		Map<String, Integer> genderTotal = new HashMap<>();
		String genderTotalMan = "男";
		String genderTotalWom = "女";
		String genderTotalOth = "其他";

		Map<String, Integer> cityTotal = new HashMap<>();
		Map<String, Integer> pointTotal = new HashMap<>();
		Map<String, Integer> ageTotal = new HashMap<>();
		Map<String, Integer> tagTotal = new HashMap<>();
		for (Inno72MerchantTotalCountByUser user:users){
			String sex = Optional.of(user.getSex()).orElse("");
			if (sex.equals(genderTotalMan)){
				Integer integer = genderTotal.get(genderTotalMan);
				if (integer == null){
					integer = 0;
				}
				integer += 1;
				genderTotal.put(genderTotalMan, integer);
			}else if (sex.equals(genderTotalWom)){
				Integer integer = genderTotal.get(genderTotalWom);
				if (integer == null){
					integer = 0;
				}
				integer += 1;
				genderTotal.put(genderTotalWom, integer);
			}else {
				Integer integer = genderTotal.get(genderTotalOth);
				if (integer == null){
					integer = 0;
				}
				integer += 1;
				genderTotal.put(genderTotalOth, integer);
			}

			String city = user.getCity();
			if (StringUtil.notEmpty(city)){
				Integer integer = cityTotal.get(city);
				if (integer == null){
					integer = 0;
				}
				integer+=1;
				cityTotal.put(city, integer);
			}

			String pointTag = user.getPointTag();
			if (StringUtil.notEmpty(pointTag)){
				Integer integer = pointTotal.get(pointTag);
				if (integer == null){
					integer = 0;
				}
				integer+=1;
				pointTotal.put(pointTag, integer);
			}

			Integer age = user.getAge();
			if (age != null){
				if (age <= 10){
					Integer integer = ageTotal.get("1");
					if (integer == null){
						integer = 0;
					}
					integer += 1;
					ageTotal.put("1", integer);
				}else if(age <= 20){
					Integer integer = ageTotal.get("2");
					if (integer == null){
						integer = 0;
					}
					integer += 1;
					ageTotal.put("2", integer);
				}else if(age <= 30){
					Integer integer = ageTotal.get("3");
					if (integer == null){
						integer = 0;
					}
					integer += 1;
					ageTotal.put("3", integer);
				}else if(age <= 40){
					Integer integer = ageTotal.get("4");
					if (integer == null){
						integer = 0;
					}
					integer += 1;
					ageTotal.put("4", integer);
				}else if(age <= 50){
					Integer integer = ageTotal.get("5");
					if (integer == null){
						integer = 0;
					}
					integer += 1;
					ageTotal.put("5", integer);
				}else if(age <= 60){
					Integer integer = ageTotal.get("6");
					if (integer == null){
						integer = 0;
					}
					integer += 1;
					ageTotal.put("6", integer);
				}else {
					Integer integer = ageTotal.get("7");
					if (integer == null){
						integer = 0;
					}
					integer += 1;
					ageTotal.put("7", integer);
				}
			}

			String userTag = user.getUserTag();
			JSONArray jsonArray = JSON.parseArray(userTag);
			for (Object jj : jsonArray){
				if (StringUtil.notEmpty(jj)){
					String tag = jj.toString();
					Integer integer = tagTotal.get(tag);
					if (integer == null){
						integer = 0;
					}
					integer += 1;
					tagTotal.put(tag, integer);
				}
			}
		}

		long totalGenderNum = 0;
		for (Map.Entry<String, Integer> entry : genderTotal.entrySet()){
			totalGenderNum += entry.getValue();
		}
		List<Map<String, Object>> userSex = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : genderTotal.entrySet()){
			Integer value = entry.getValue();
			String key = entry.getKey();
			BigDecimal multiply = BigDecimal.valueOf(value)
					.divide(BigDecimal.valueOf(totalGenderNum), 2, BigDecimal.ROUND_HALF_EVEN)
					.multiply(new BigDecimal("100"));
			Map<String, Object> map = new HashMap<>();
			map.put("name", key);
			map.put("y", multiply.intValue());
			map.put("no", value);
			userSex.add(map);
		}

		long totalCityNum = 0;
		for (Map.Entry<String, Integer> entry : cityTotal.entrySet()){
			totalCityNum += entry.getValue();
		}
		List<Map<String, Object>> city = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : cityTotal.entrySet()){
			Integer value = entry.getValue();
			String key = entry.getKey();
			BigDecimal multiply = BigDecimal.valueOf(value)
					.divide(BigDecimal.valueOf(totalCityNum), 2, BigDecimal.ROUND_HALF_EVEN)
					.multiply(new BigDecimal("100"));
			Map<String, Object> map = new HashMap<>();
			map.put("name", key);
			map.put("y", multiply.intValue());
			map.put("no", value);
			city.add(map);
		}

		long totalPointNum = 0;
		for (Map.Entry<String, Integer> entry : pointTotal.entrySet()){
			totalPointNum += entry.getValue();
		}
		List<Map<String, Object>> point = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : pointTotal.entrySet()){
			Integer value = entry.getValue();
			String key = entry.getKey();
			BigDecimal multiply = BigDecimal.valueOf(value)
					.divide(BigDecimal.valueOf(totalPointNum), 2, BigDecimal.ROUND_HALF_EVEN)
					.multiply(new BigDecimal("100"));
			Map<String, Object> map = new HashMap<>();
			map.put("name", key);
			map.put("y", multiply.intValue());
			map.put("no", value);
			point.add(map);
		}

		List<String> ageStage = new ArrayList<>();
		ageStage.add("10岁以下");
		ageStage.add("11-20");
		ageStage.add("21-30");
		ageStage.add("31-40");
		ageStage.add("41-50");
		ageStage.add("51-60");
		ageStage.add("60岁以上");
		List<Integer> ageStageNum = new ArrayList<>();
		ageStageNum.add(Optional.ofNullable(ageTotal.get("1")).orElse(0));
		ageStageNum.add(Optional.ofNullable(ageTotal.get("2")).orElse(0));
		ageStageNum.add(Optional.ofNullable(ageTotal.get("3")).orElse(0));
		ageStageNum.add(Optional.ofNullable(ageTotal.get("4")).orElse(0));
		ageStageNum.add(Optional.ofNullable(ageTotal.get("5")).orElse(0));
		ageStageNum.add(Optional.ofNullable(ageTotal.get("6")).orElse(0));
		ageStageNum.add(Optional.ofNullable(ageTotal.get("7")).orElse(0));
		Map<String, Object> userAge = new HashMap<>();
		userAge.put("x", ageStage);
		userAge.put("y", ageStageNum);

		List<String> tagNames = new ArrayList<>();
		List<Integer> tagNum = new ArrayList<>();
		for (Map.Entry<String, Integer> tag: tagTotal.entrySet()){
			tagNames.add(tag.getKey());
			tagNum.add(tag.getValue());
		}
		Map<String, Object> userLabel = new HashMap<>();
		userAge.put("x", tagNames);
		userAge.put("y", tagNum);

		Map<String, Object> result = new HashMap<>();
		result.put("userSex", userSex);
		result.put("point", point);
		result.put("city", city);
		result.put("userAge", userAge);
		result.put("userlabel", userLabel);

		return Results.success(result);
	}
}
