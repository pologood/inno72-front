package com.inno72.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
			String goods, String merchantId) {

		if (StringUtil.isEmpty(activityId) || StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)
				|| StringUtil.isEmpty(merchantId)) {
			return Results.failure("参数缺失!");
		}
		LOGGER.info("查询列表 -> label - {}, activityId - {}, city - {}, startDate - {}, endDate - {}, goods - {}", label,
				activityId, city, startDate, endDate, goods);
		LocalDate startDateLocal = LocalDateUtil.transfer(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate endDateLocal = LocalDateUtil.transfer(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		if (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear() > 90) {
			return Results.failure("不能大于三个月!");
		}

		List<Inno72MerchantTotalCountByDay> days = inno72MerchantTotalCountByDayMapper.selectList(activityId, city,
				startDate, endDate, goods, merchantId);
		Map<String, Object> result;
		switch (label) {
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

	@Override
	public byte[] getBytes(Map<String, Object> data, String body, String label) {

		switch (body) {
			case "table":
				return this.getTable(data.get("list") == null ? new byte[0] : data.get("list"), label);
			case "chart":
				return this.getCharts(data.get("chart") == null ? new byte[0] : data.get("chart"), label);
		}

		return new byte[0];
	}

	private byte[] getCharts(Object chart, String label) {
		return new byte[0];
	}

	private byte[] getTable(Object list, String label) {

		switch (label) {
			case "order":
				return this.buildOrderExcel(list);
			case "goods":
				return this.buildGoodsExcel(list);
			case "user":
				return this.buildUserExcel(list);
		}
		return new byte[0];
	}


	private Map<String, Object> buildOrder(List<Inno72MerchantTotalCountByDay> days, LocalDate startDateLocal,
			LocalDate endDateLocal) {
		Map<String, Object> result = new HashMap<>(2);
		// chart list
		result.put("list", days);

		days.sort(Comparator.comparing(Inno72MerchantTotalCountByDay::getDate));

		// 处理charts部分数据

		// 按日期分组所有数据 key = 日期 ； value 分组的数据
		Map<String, List<Inno72MerchantTotalCountByDay>> kk = kk(days, startDateLocal, endDateLocal);

		LocalDate thisDate = startDateLocal;

		Map<String, Object> y = new HashMap<>(6);
		List<Integer> orderQtyTotalS = new ArrayList<>(); // 数量
		List<Integer> orderQtySuccS = new ArrayList<>(); // 数量
		List<Integer> goodsNumS = new ArrayList<>(); // 数量
		List<Integer> uvS = new ArrayList<>(); // 数量
		List<Integer> pvS = new ArrayList<>(); // 数量
		List<Integer> couponNumS = new ArrayList<>(); // 数量

		// 统计单日下所有数量的总和
		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> k : kk.entrySet()) {

			if (StringUtil.isEmpty(k.getKey())) {
				continue;
			}

			List<Inno72MerchantTotalCountByDay> value = k.getValue();
			int orderQtyTotal = 0;
			int goodsNum = 0;
			int pv = 0;
			int couponNum = 0;
			int uv = 0;
			int orderQtySucc = 0;
			for (Inno72MerchantTotalCountByDay day : value) {

				goodsNum += day.getGoodsNum();
				orderQtyTotal += day.getOrderQtyTotal();
				orderQtySucc += day.getOrderQtySucc();
				pv += day.getPv();
				uv += day.getUv();
				couponNum += day.getCouponNum();
			}

			LocalDate cDate = LocalDateUtil.transfer(k.getKey(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			// 计算日差，如果当前日期数据小于传入的开始日期。补充0的数据到数组
			while (thisDate.isBefore(cDate)) {
				orderQtyTotalS.add(0);
				orderQtySuccS.add(0);
				goodsNumS.add(0);
				uvS.add(0);
				pvS.add(0);
				couponNumS.add(0);
				thisDate = thisDate.plusDays(1);
			}
			// 指针日期加一天
			thisDate = thisDate.plusDays(1);

			// 归类数据
			orderQtyTotalS.add(orderQtyTotal);
			orderQtySuccS.add(orderQtySucc);
			goodsNumS.add(goodsNum);
			uvS.add(uv);
			pvS.add(pv);
			couponNumS.add(couponNum);

		}

		// 日期不足，补充0直到到结束日期
		while (orderQtyTotalS.size() <= (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear())) {
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

	private byte[] buildOrderExcel(Object list) {
		List<Inno72MerchantTotalCountByDay> days = (List<Inno72MerchantTotalCountByDay>) list;

		List<List<Object>> data = new ArrayList<>();
		List<Object> totelRow = new ArrayList<>();
		totelRow.add("日期");
		totelRow.add("地区");
		totelRow.add("商品名称");
		totelRow.add("互动次数");
		totelRow.add("互动人数");
		totelRow.add("订单数");
		totelRow.add("支付成功数");
		totelRow.add("派发商品数");
		totelRow.add("派发优惠券数");
		data.add(totelRow);
		for (Inno72MerchantTotalCountByDay day : days) {
			List<Object> row = new ArrayList<>();

			row.add(day.getDate());
			row.add(day.getCity());
			row.add(day.getGoodsName());
			row.add(day.getPv());
			row.add(day.getUv());
			row.add(day.getOrderQtyTotal());
			row.add(day.getOrderQtySucc());
			row.add(day.getGoodsNum());
			row.add(day.getCouponNum());
			data.add(row);
		}
		return buildExcel(data);
	}

	private Map<String, List<Inno72MerchantTotalCountByDay>> kk(List<Inno72MerchantTotalCountByDay> days,
			LocalDate startDate, LocalDate endDate) {
		days.sort(Comparator.comparing(Inno72MerchantTotalCountByDay::getDate));
		Map<String, List<Inno72MerchantTotalCountByDay>> map = new TreeMap<>();
		for (Inno72MerchantTotalCountByDay day : days) {
			String date = day.getDate();
			List<Inno72MerchantTotalCountByDay> byDays = map.get(date);
			if (byDays == null) {
				byDays = new ArrayList<>();
			}
			byDays.add(day);
			map.put(date, byDays);
		}
		return map;
	}


	private Map<String, Object> buildUser(List<Inno72MerchantTotalCountByDay> days, LocalDate startDateLocal,
			LocalDate endDateLocal) {
		Map<String, Object> result = new HashMap<>(2);
		Map<String, List<Inno72MerchantTotalCountByDay>> map = new HashMap<>();
		this.groupByCityAndDate(days, map);
		LOGGER.info("分组后的map -> {}", JSON.toJSONString(map));
		List<Map<String, String>> list = new ArrayList<>();
		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> entry : map.entrySet()) {
			List<Inno72MerchantTotalCountByDay> value = entry.getValue();

			Double totleStay = (double)0;
			Double totleConcernNum = (double)0;
			String city = value.get(0).getCity();
			String date = value.get(0).getDate();

			for (Inno72MerchantTotalCountByDay day : value) {
				totleStay += day.getStayNum();
				totleConcernNum += day.getConcernNum();
			}
			Map<String, String> userResult = new HashMap<>(4);
			userResult.put("date", date);
			userResult.put("city", city);
			userResult.put("experience", totleStay + "");
			userResult.put("concern", totleConcernNum + "");
			if (totleConcernNum != 0 && totleStay != 0) {
				BigDecimal divide = new BigDecimal(totleConcernNum).divide(new BigDecimal(totleStay), 2,
						BigDecimal.ROUND_CEILING);
				userResult.put("percent", divide.toString());
			} else {
				userResult.put("percent", "0");
			}
			list.add(userResult);
		}

		result.put("list", list);

		Map<String, List<Map<String, String>>> kk = kku(list, startDateLocal, endDateLocal);

		List<Double> experienceS = new ArrayList<>();
		List<Double> concernS = new ArrayList<>();
		List<Double> percentS = new ArrayList<>();

		LocalDate thisDate = startDateLocal;
		// 统计单日下所有数量的总和
		for (Map.Entry<String, List<Map<String, String>>> k : kk.entrySet()) {

			if (StringUtil.isEmpty(k.getKey())) {
				continue;
			}
			List<Map<String, String>> value = k.getValue();

			double experience = 0;
			double concern = 0;
			double percent = 0;

			for (Map<String, String> day : value) {

				experience +=  Double.valueOf(day.get("experience"));
				concern +=  Double.valueOf(day.get("concern"));
				percent += Double.valueOf(day.get("percent"));
			}
			LocalDate cDate = LocalDateUtil.transfer(k.getKey(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			// 计算日差，如果当前日期数据小于传入的开始日期。补充0的数据到数组
			while (thisDate.isBefore(cDate)) {
				experienceS.add((double)0);
				concernS.add((double)0);
				percentS.add((double)0);
				thisDate = thisDate.plusDays(1);
			}
			// 指针日期加一天
			thisDate = thisDate.plusDays(1);

			// 归类数据
			experienceS.add(experience);
			concernS.add(concern);
			percentS.add(percent);

		}

		// 日期不足，补充0直到到结束日期
		while (experienceS.size() <= (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear())) {
			experienceS.add((double)0);
			concernS.add((double)0);
			percentS.add((double)0);
		}

		Map<String, List<Double>> ys = new HashMap<>();
		ys.put("experienceS", experienceS);
		ys.put("percentS", percentS);
		ys.put("concernS", concernS);
		result.put("chart", ys);

		return result;
	}

	private byte[] buildUserExcel(Object object) {
		List<Map<String, String>> list = (List<Map<String, String>>) object;
		List<List<Object>> result = new ArrayList<>();
		List<Object> r = new ArrayList<>();
		r.add("日期");
		r.add("城市");
		r.add("体验用户数");
		r.add("关注人数");
		r.add("百分比");
		result.add(r);
		for (Map<String, String> map : list) {

			String date = map.get("date");
			String city = map.get("city");
			String experience = map.get("experience");
			String concern = map.get("concern");
			String percent = map.get("percent");
			List<Object> rs = new ArrayList<>();
			rs.add(date);
			rs.add(city);
			rs.add(experience);
			rs.add(concern);
			rs.add(percent);
			result.add(rs);

		}


		return buildExcel(result);
	}


	private Map<String, List<Map<String, String>>> kku(List<Map<String, String>> list, LocalDate startDateLocal,
			LocalDate endDateLocal) {
		list.sort(Comparator.comparing((v) -> v.get("date")));

		Map<String, List<Map<String, String>>> map = new TreeMap<>();
		for (Map<String, String> day : list) {
			String date = day.get("date");
			List<Map<String, String>> byDays = map.get(date);
			if (byDays == null) {
				byDays = new ArrayList<>();
			}
			byDays.add(day);
			map.put(date, byDays);
		}
		return map;
	}

	private void groupByCityAndDate(List<Inno72MerchantTotalCountByDay> days,
			Map<String, List<Inno72MerchantTotalCountByDay>> map) {
		for (Inno72MerchantTotalCountByDay day : days) {
			String city = day.getCity();
			String date = day.getDate();

			String key = city + date;
			List<Inno72MerchantTotalCountByDay> value = map.get(key);
			if (value == null) {
				value = new ArrayList<>();
			}
			value.add(day);
			map.put(key, value);
		}
	}

	private Map<String, Object> buildGoods(List<Inno72MerchantTotalCountByDay> days, LocalDate startDateLocal,
			LocalDate endDateLocal) {
		Map<String, Object> result = new HashMap<>(2);
		result.put("list", days);

		Map<String, List<Inno72MerchantTotalCountByDay>> map = new HashMap<>();
		this.groupByCityAndDate(days, map);

		List<Inno72MerchantTotalCountByDay> resultList = new ArrayList<>();
		List<Map<String, String>> pvuvMap = new ArrayList<>();
		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> entry : map.entrySet()) {
			List<Inno72MerchantTotalCountByDay> value = entry.getValue();

			int totlePv = 0;
			int totleUv = 0;
			String date = "";
			for (Inno72MerchantTotalCountByDay day : value) {
				totlePv += day.getPv();
				totleUv += day.getUv();
				if (StringUtil.isEmpty(date) && StringUtil.notEmpty(day.getDate())) {
					date = day.getDate();
				}
			}
			for (Inno72MerchantTotalCountByDay day : value) {
				day.setPv(totlePv);
				day.setUv(totleUv);
			}
			Map<String, String> pvuv = new HashMap<>(2);
			pvuv.put("pv", totlePv + "");
			pvuv.put("uv", totleUv + "");
			pvuv.put("date", date);
			pvuvMap.add(pvuv);
			resultList.addAll(value);
		}
		result.put("list", resultList);
		pvuvMap.sort(Comparator.comparing(o -> o.get("date")));

		List<Integer> pvs = new ArrayList<>();
		List<Integer> uvs = new ArrayList<>();
		LocalDate addDateLocal = startDateLocal;
		for (Map<String, String> e : pvuvMap) {
			String pv = e.get("pv");
			String uv = e.get("uv");
			String date = e.get("date");
			LocalDate thisDate = LocalDateUtil.transfer(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			while (addDateLocal.isBefore(thisDate)) {
				pvs.add(0);
				uvs.add(0);
				addDateLocal = addDateLocal.plusDays(1);
				LOGGER.info("商品维度 日期 - {}, uvs - {}, pvs - {}", addDateLocal, uvs, pvs);
			}
			pvs.add(Integer.parseInt(pv));
			uvs.add(Integer.parseInt(uv));
			addDateLocal = addDateLocal.plusDays(1);
		}
		while (pvs.size() <= (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear())) {
			pvs.add(0);
			uvs.add(0);
		}


		Map<String, List<Inno72MerchantTotalCountByDay>> groupByGoodsId = new HashMap<>();
		for (Inno72MerchantTotalCountByDay day : days) {
			String goodsId = day.getGoodsId();
			List<Inno72MerchantTotalCountByDay> byDays = groupByGoodsId.get(goodsId);
			if (byDays == null) {
				byDays = new ArrayList<>();
			}
			byDays.add(day);
			groupByGoodsId.put(goodsId, byDays);
		}

		List<List<Integer>> nums = new ArrayList<>();
		List<Map<String, String>> names = new ArrayList<>();
		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> entry : groupByGoodsId.entrySet()) {
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
			for (Inno72MerchantTotalCountByDay inno72MerchantTotalCountByDay : value) {
				String date = inno72MerchantTotalCountByDay.getDate();
				LocalDate thisDate = LocalDateUtil.transfer(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				while (curLocalDate.isBefore(thisDate)) {
					num.add(0);
					curLocalDate = curLocalDate.plusDays(1);
					LOGGER.info("商品维度 日期 - {}, num - {}", curLocalDate, num);
				}

				num.add(inno72MerchantTotalCountByDay.getGoodsNum());
				curLocalDate = curLocalDate.plusDays(1);
				LOGGER.info("商品维度 日期 - {}, num - {}", curLocalDate, num);
			}

			// 日期不足，补充0直到到结束日期
			while (num.size() <= (endDateLocal.getDayOfYear() - startDateLocal.getDayOfYear())) {
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
		result.put("chart", charts);
		return result;
	}

	private byte[] buildGoodsExcel(Object list) {


		List<Inno72MerchantTotalCountByDay> days = (List<Inno72MerchantTotalCountByDay>) list;

		LOGGER.info("构建商品统计数据 -》 {}", JSON.toJSONString(days));

		Map<String, List<Inno72MerchantTotalCountByDay>> dayAndCityAndGoods = new HashMap<>();

		if (days.size() == 0) {
			return new byte[0];
		}

		for (Inno72MerchantTotalCountByDay day : days) {
			String date = day.getDate();
			String city = day.getCity();
			String key = date + city;
			List<Inno72MerchantTotalCountByDay> days1 = dayAndCityAndGoods.get(key);
			if (days1 == null) {
				days1 = new ArrayList<>();
			}
			days1.add(day);
			dayAndCityAndGoods.put(key, days1);
		}

		Set<String> goodsIds = new TreeSet<>();
		List<List<Object>> numss = new ArrayList<>();
		Set<String> goodsNames = new TreeSet<>();
		Map<String, String> map = new TreeMap<>();

		for (Map.Entry<String, List<Inno72MerchantTotalCountByDay>> m : dayAndCityAndGoods.entrySet()) {

			List<Inno72MerchantTotalCountByDay> value = m.getValue();

			Map<String, Integer> goods = new HashMap<>();
			Inno72MerchantTotalCountByDay day = value.get(0);

			String city = day.getCity();
			String date = day.getDate();
			Integer pv = day.getPv();
			Integer uv = day.getUv();

			for (Inno72MerchantTotalCountByDay byDay : value) {
				String goodsId = byDay.getGoodsId();
				Integer goodsNum = byDay.getGoodsNum();
				goodsIds.add(goodsId);
				goods.put(goodsId, goodsNum);
				goodsNames.add(byDay.getGoodsName());
			}


			List<Object> nums = new ArrayList<>();
			nums.add(date);
			nums.add(city);
			nums.add(pv);
			nums.add(uv);
			for (String goodsId : goodsIds) {
				Integer num = goods.get(goodsId);
				nums.add(num);
			}
			numss.add(nums);
		}

		List<List<Object>> re = new ArrayList<>();
		Set<Entry<String, String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			System.out.println(entry);
		}
		List<Object> sList = new ArrayList<>();
		sList.add("日期");
		sList.add("城市");
		sList.add("互动次数");
		sList.add("互动人数");
		List<Object> asList = Arrays.asList(goodsNames.toArray());
		for (int i = 0; i < asList.size(); i++) {
			sList.add(asList.get(asList.size() - i - 1));
		}
		re.add(sList);
		re.addAll(numss);

		return buildExcel(re);
	}


	private byte[] buildExcel(List<List<Object>> values) {
		try {
			Workbook wb = new XSSFWorkbook();
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			Sheet sheet = wb.createSheet("new sheet");
			for (int i = 0; i < values.size(); i++) {
				Row row = sheet.createRow((short) i);
				List<Object> rowValues = values.get(i);
				for (int j = 0; j < rowValues.size(); j++) {
					row.createCell(j).setCellValue(rowValues.get(j) == null ? "" : rowValues.get(j).toString());
				}
			}
			wb.write(out);
			byte[] a = out.toByteArray();
			out.close();
			return a;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
