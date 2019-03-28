package com.inno72.monitor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.UuidUtil;
import com.inno72.common.util.excel.ExportExcel;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.PointsMapper;
import com.inno72.model.Content;
import com.inno72.model.PointPlan;
import com.inno72.model.Points;
import com.inno72.model.RequestBody;
import com.inno72.model.ResponseBody;
import com.inno72.redis.IRedisUtil;

@Component
public class PointPlanTask {

	private static Logger LOGGER = LoggerFactory.getLogger(PointPlanTask.class);

	@Resource
	private MongoOperations mongoOperations;

	@Resource
	private IRedisUtil redisUtil;


	static int no = 1;

	private static final String response = "fcbox-response-country-all-500m";
	private static final String request = "fcbox-request-country-all-500m";
	private static final String fileName = "fcbox-country-all-500.xlsx";
	private static final String pointPlan = "FcBoxPointPlan-500";
	// 范围内 检索半径 0.01 = 1000m
	private static final Double km1 = 1d;
	private static final Double km2 = 0.00570d;
	private static final Double km3 = 0.00450d;

	// 左上 经度 73.113659,53.238915
	//	private static double latLeft = 53.238915;
	private static double latLeft = 53.238915;
	// 左上 纬度 33.022165298944095
	private static double lonLeft = 73.113659;
	// 右下 经度 135.340222,3.710773
	private static double latRight = 3.710773;
	// 右下 纬度
	private static double lonRight = 135.340222;

	@Resource
	private MapsUtil mapsUtil;

	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
	private Semaphore semaphore = new Semaphore(20);

	public void test() {
		LOGGER.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -- 开始执行 {} -- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%", LocalDateTime.now());
		int i = 0;
		while (latLeft  > latRight) {
			Double cLongLeft = lonLeft;
			List<PointPlan> plans = new LinkedList<>();
			while (cLongLeft < lonRight) {
				i++;
				PointPlan pointPlan = new PointPlan(i, latLeft, cLongLeft);
				LOGGER.info("plan ----> {}", JSON.toJSONString(pointPlan));
				plans.add(pointPlan);
				if (plans.size() >= 500){
					fixedThreadPool.execute(new CheckRunner(plans, mongoOperations, PointPlanTask.pointPlan));
					plans = new LinkedList<>();
				}
				cLongLeft += km2;
			}
			if (plans.size() > 0){
				fixedThreadPool.execute(new CheckRunner(plans, mongoOperations, PointPlanTask.pointPlan));
				plans = new LinkedList<>();
			}

			latLeft -= km3;
		}

		LOGGER.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -- 重新循环 {} -- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
				LocalDateTime.now());
	}

	@Autowired
	private PointsMapper pointsMapper;

	public void test2() {
		long total = mongoOperations.count(new Query(Criteria.where("content").exists(true)), response);
 		int perPage = 10000;

		long pageSize = total / perPage + (total % perPage != 0 ? 1 : 0);

		int totleNum = 0;

		Set<String> names = new HashSet<>();


		Query pointLogQuery = new Query();
		pointLogQuery.addCriteria(Criteria.where("content").exists(true));
		pointLogQuery.with(new Sort(Sort.Direction.ASC, "_id"));
		pointLogQuery.limit(perPage);
		int namesSize = names.size();
//		List<String> headerList = new ArrayList<String>();
//		headerList.add("服务类型");
//		headerList.add("地址");
//		headerList.add("服务时段");
//		headerList.add("所在经度");// 116
//		headerList.add("所在纬度");// 40
//		ExportExcel excelShop = new ExportExcel("", headerList);

		Points points;
		List<Points> pointss = new LinkedList<>();
		for (int i = 0; i < pageSize; i++) {

			pointLogQuery.skip(i * perPage);

			List<ResponseBody> machineInfomations = mongoOperations.find(pointLogQuery, ResponseBody.class, response);

			for (ResponseBody body : machineInfomations) {
				List<Content> contents = body.getContent();

				if (contents != null && contents.size() > 0) {
					totleNum += contents.size();

					for (Content content : contents) {
						names.add(content.getServiceType() + content.getAddress());
						int cNamesSize = names.size();
						if (namesSize + 1 == cNamesSize) {
//							Row row = excelShop.addRow();
//							excelShop.addCell(row, 0, content.getServiceType());
//							excelShop.addCell(row, 1, content.getAddress());
//							excelShop.addCell(row, 2, content.getServiceTime());
//							excelShop.addCell(row, 3, content.getLongitude());
//							excelShop.addCell(row, 4, content.getLatitude());
							points = new Points();
							points.setAddress(content.getAddress());
							points.setTimes(content.getServiceTime());
							points.setType(content.getServiceType());
							points.setLat(content.getLatitude());
							points.setLon(content.getLongitude());
							points.setId(StringUtil.uuid());
							pointss.add(points);

							LOGGER.info("有效结果 -----------------------> \n              {}", JSON.toJSONString(points));

							if (pointss.size() == 500){
								pointsMapper.insertS(pointss);
								pointss = new LinkedList<>();
							}
						}

						namesSize = cNamesSize;

					}
				}
			}


		}
		if (pointss.size() > 0){
			pointsMapper.insertList(pointss);
		}
//		excelShop.writeFile("/Users/zb.zhou/Desktop/" + fileName);
		int ffz = 0;
		int kdg = 0;
		int o = 0;
		String ffzcn = "丰巢服务站";
		String kdgcn = "丰巢快递柜";
		for (String s : names) {
			if (s.startsWith(ffzcn)) {
				ffz += 1;
			} else if (s.startsWith(kdgcn)) {
				kdg += 1;
			} else {
				o += 1;
			}
		}

		System.out.println(ffz);
		System.out.println(kdg);
		System.out.println(o);
		System.out.println(totleNum);
		System.out.println(names.size());

	}

	public void test3() throws IOException {

		LOGGER.info("########################################### start test3 ############################################");

		List<Points> points = pointsMapper.selectAll();
		List<String> headerList = new ArrayList<String>();
		headerList.add("服务类型");
		headerList.add("地址");
		headerList.add("服务时段");
		headerList.add("所在经度");// 116
		headerList.add("所在纬度");// 40
		ExportExcel excelShop = new ExportExcel("", headerList);
		Map<String, List<Points>> result = new HashMap<>();
		for (Points point : points){
			String address = point.getAddress();
			String substring = address.substring(0, 2);
			if (substring.contains("重庆")){
				substring = "重庆";
			}
			else if(substring.contains("上海")){
				substring = "上海";
			}
			else if (substring.contains("北京")){
				substring = "北京";
			}
			else if (substring.contains("宁夏")){
				substring = "宁夏";
			}
			else if (substring.contains("天津")){
				substring = "天津";
			}

			Row row = excelShop.addRow(substring);
			excelShop.addCell(row, 0, point.getType());
			excelShop.addCell(row, 1, point.getAddress());
			excelShop.addCell(row, 2, point.getTimes());
			excelShop.addCell(row, 3, point.getLon());
			excelShop.addCell(row, 4, point.getLat());
		}

		excelShop.writeFile("/Users/zb.zhou/Desktop/" + fileName);

		LOGGER.info("########################################### end test3 ############################################");

	}


	private void getHttp(Map<String, String> map) throws IOException {
		String lon = map.get("lon");
		String lat = map.get("lat");
		int pageNo = Optional.ofNullable(map.get("pageNo")).map(Integer::parseInt).orElse(1);
		String ip = getRandomIp();
		String httpResp = getHttpResp(genFcBoxUrl(lon, lat, pageNo + "", ip), ip);

		String id = StringUtil.uuid();
		/**
		 * @Param requestId id
		 * @param ip
		 * @param lat
		 * @param lon
		 * @param page
		 * @param responseBodies
		 */
		saveHeaders(new RequestBody(no += 1, id, ip, lon, lat, pageNo + ""));
		// {"code":0,"engDesc":"success","chnDesc":"操作成功","detail":"getLocationNearService操作成功","content":null,"totalCount":null,"pageSize":10,"pageNo":1}
		if (StringUtils.isNotEmpty(httpResp)) {
			try {
				ResponseBody responseBody = JSON.parseObject(httpResp, new TypeReference<ResponseBody>() {
				});
				responseBody.setRequestId(id);
				responseBody.setId(StringUtil.uuid());

				saveBody(responseBody);

				String totalCount = responseBody.getTotalCount();
				if (StringUtil.notEmpty(totalCount)) {
					int i5 = Integer.parseInt(totalCount);
					int totalPage = i5 / 10 + 1;
					if (responseBody.getContent() != null)
						if (pageNo < totalPage) {
							map.put("pageNo", pageNo + 1 + "");
							getHttp(map);
						}
				}


			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void saveHeaders(RequestBody body) throws IOException {
		String lat = body.getLat();
		String lon = body.getLon();

		String json = Optional.ofNullable(getHttpResp(getGaodeUrl(lon + "," + lat), getRandomIp())).orElse("");
		String formatted_address = Optional.ofNullable(FastJsonUtils.getString(json, "formatted_address")).orElse("");
		String addressComponent = Optional.ofNullable(FastJsonUtils.getString(json, "addressComponent"))
				.map(Object::toString).orElse("");
		JSONObject jsonObject = JSON.parseObject(addressComponent);

		if (jsonObject != null) {
			String province = Optional.ofNullable(jsonObject.get("province")).map(Object::toString).orElse("");
			String city = Optional.ofNullable(jsonObject.get("city")).map(Object::toString).orElse("");
			String district = Optional.ofNullable(jsonObject.get("district")).map(Object::toString).orElse("");
			String township = Optional.ofNullable(jsonObject.get("township")).map(Object::toString).orElse("");
			body.setFormatted_address(formatted_address);
			body.setProvince(province);
			body.setCity(city);
			body.setDistrict(district);
			body.setTownship(township);
		}

		mongoOperations.insert(body, request);
	}

	private void saveBody(ResponseBody body) {
		mongoOperations.insert(body, response);
	}

	private String genFcBoxUrl(String lon, String lat, String pageNo, String ip) {
		return "https://www.fcbox.com/serviceNodeQuery/nearServiceNode?longitude=" + lat + "&latitude=" + lon
				+ "&type=&pageNo=" + pageNo + "&_=1552045644895";
	}

	private static String getHttpResp(String url, String ip) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		/* 设置超时 */
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).build();
		httpPost.setConfig(defaultRequestConfig);
		httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.addHeader("x-forwarded-for", ip);
		CloseableHttpResponse response = null;
		String result = null;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
			LOGGER.info(result);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null)
				response.close();
			httpClient.close();
		}
	}

	/**
	 * 统计 起始坐标与终点坐标系内 每1公里的切块坐标
	 * @return
	 */
	private List<Map<String, String>> latLon() {
		List<Map<String, String>> param = new ArrayList<>();

		while ((latLeft += km1) < latRight + km1) {
			Double cLongLeft = lonLeft;
			while ((cLongLeft -= km1) > lonRight) {
				Map<String, String> cMap = new HashMap<>();
				cMap.put("lon", cLongLeft + "");
				cMap.put("lat", latLeft + "");
				param.add(cMap);
				if (param.size() == 8000) {
					FcBoxPointCountryControl countryControl = new FcBoxPointCountryControl(UuidUtil.getUUID32(), param);
					saveIndex(countryControl);
					param = new ArrayList<>();
				}
			}
		}
		FcBoxPointCountryControl countryControl = new FcBoxPointCountryControl(UuidUtil.getUUID32(), param);
		saveIndex(countryControl);

		return param;
	}

	public void saveIndex(FcBoxPointCountryControl countryControl) {

		FcBoxPointCountryControlIndex index = new FcBoxPointCountryControlIndex(countryControl.getId());
		mongoOperations.save(countryControl, "FcBoxPointCountryControl");
		mongoOperations.save(index, "FcBoxPointCountryControlIndex");
	}

	public static String getGaodeUrl(String latLon) {
		return "https://restapi.amap.com/v3/geocode/regeo?output=json&location=" + latLon
				+ "&key=fda2b506f0e0875980f1c66a2183c17b&radius=1000&extensions=all";
	}

	/**
	 * https://restapi.amap.com/v3/geocode/regeo?output=xml&location=115.663346,40.501278&key=fda2b506f0e0875980f1c66a2183c17b&radius=1000&extensions=all
	 */
	private void getAddress(String lat, String lon) throws IOException {
		System.out.println(getHttpResp(getGaodeUrl(lat + "," + lon), getRandomIp()));
	}


	public static String getRandomIp() {
		// ip范围
		int[][] range = {{607649792, 608174079}, // 36.56.0.0-36.63.255.255
				{1038614528, 1039007743}, // 61.232.0.0-61.237.255.255
				{1783627776, 1784676351}, // 106.80.0.0-106.95.255.255
				{2035023872, 2035154943}, // 121.76.0.0-121.77.255.255
				{2078801920, 2079064063}, // 123.232.0.0-123.235.255.255
				{-1950089216, -1948778497}, // 139.196.0.0-139.215.255.255
				{-1425539072, -1425014785}, // 171.8.0.0-171.15.255.255
				{-1236271104, -1235419137}, // 182.80.0.0-182.92.255.255
				{-770113536, -768606209}, // 210.25.0.0-210.47.255.255
				{-569376768, -564133889}, // 222.16.0.0-222.95.255.255
		};

		Random rdint = new Random();
		int index = rdint.nextInt(10);
		String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
		return ip;
	}

	public static String num2ip(int ip) {
		int[] b = new int[4];
		String x = "";

		b[0] = (ip >> 24) & 0xff;
		b[1] = (ip >> 16) & 0xff;
		b[2] = (ip >> 8) & 0xff;
		b[3] = ip & 0xff;
		x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer
				.toString(b[3]);

		return x;
	}

}

