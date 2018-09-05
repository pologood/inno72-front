package com.inno72.service.impl;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.service.SuperOpenService;

@Service
public class SuperOpenServiceImpl implements SuperOpenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SuperOpenServiceImpl.class);

	/**
	 * @param requestJson
	 *
	 *
	 *	{
	 *
	 *	   "serviceName":findGame,
	 *	   "params":{
	 *	        "machineId":"machineId",
	 *	        "gameId":"gameId"
	 *	    },
	 *	    "version":"1.0.0"
	 *	}
	 */
	private static final String SERVICE_NAME_KEY = "serviceName";
	private static final String VERSION_KEY = "version";
	private static final String PARAMS_KEY = "params";

	@Override
	public String adpter(String requestJson) {

		requestJson = Optional.ofNullable(requestJson).orElse("");

		LOGGER.info("inno72 开放接口 request 参数 ==> requestJson -> {} ", requestJson);
		JSONObject jsonObject = JSON.parseObject(requestJson);
		String serviceName = Optional.ofNullable(jsonObject.get(SERVICE_NAME_KEY)).map(Object::toString).orElse("");
		String version = Optional.ofNullable(jsonObject.get(VERSION_KEY)).map(Object::toString).orElse("");
		String params = Optional.ofNullable(jsonObject.get(PARAMS_KEY)).map(Object::toString).orElse("");
		ADPTE_METHOD method = ADPTE_METHOD.selectAdpteByServiceNameAndVersion(serviceName, version);

		LOGGER.debug("redirect url: {}", method);
		return method.path + this.buildRequesParams(params);
	}

	@SuppressWarnings("unused")
	public static enum ADPTE_METHOD {

		/** 生成二维码 */
		CREATE_QR_CODE("001", "createQrCode", "/session/createQrCode", "生成二维码", "1.0.0"),
		/** 获取登录信息 */
		SESSION_POLLING("002", "polling", "/session/polling", "获取登录信息", "1.0.0"),
		/** */
		FIND_GAME("003", "findGame", "/machine/findGame", "生成二维码", "1.0.0"),
		/** 获取商品信息 */
		FIND_PRODUCT("004", "findProduct", "/api/goods/findProduct", "获取商品信息", "1.0.0"),
		/** 下单  */
		CREATE_ORDER("005", "order", "/api/qroauth/paiYangOrder", "下单", "1.0.0"),
		/** 验证下单状态 */
		ORDER_POLLING("006", "orderPolling", "/api/qroauth/order-polling", "验证下单状态", "1.0.0"),
		/** 抽奖 */
		LUCKY_DRAW("007", "luckyDraw", "/api/special/luckyDraw", "抽奖", "1.0.0"),
		/** 出货后调用减货 */
		SHIPMENT_REPORT("008", "shipmentReport", "/api/goods/shipmentReport", "出货后调用减货", "1.0.0"),
		/** 没有方法 */
		ERROR_NO_METHOD("404", "ERROR_NO_METHOD", "/inno72/noMethod/open", "出货后调用减货", "1.0.0"),
		/** 版本不存在 */
		ERROR_NO_VERSION("500", "ERROR_NO_VERSION", "/inno72/noVersion/open", "出货后调用减货",
				"1.0.0"), MALFUNCTION_LOG("009", "malfunctionLog", "/api/malfunctionLog", "货道异常信息存储", "1.0.0"),
		/** 货道异常信息存储 */
		SHIPMENT_FAIL("010", "shipmentFail", "/api/shipmentFail", "掉货失败", "1.0.0"),
		/** 掉货失败 */
		USER_DURATION("011", "userDuration", "/api/userDuration", "用户互动时长", "1.0.0"),
		/* 用户互动时长 */
		GET_SAMPLING("012", "getSampling", "/api/getSampling", "获取派样商品", "1.0.0"),
		/* 获取派样商品 */
		ONE_KEY_ORDER("013", "oneKeyOrder", "/api/qroauth/oneKeyOrder", "一键下单（优惠券 and goods）", "1.0.0"),
		/* 获取派样商品 *//* 生成派样活动二维码 */
		createSamplingQrCode("014", "createSamplingQrCode", "/session/createSamplingQrCode", "生成派样活动二维码", "1.0.0"),
		/** 创建派样订单 */
		CREATE_PARYANG_ORDER("015", "paiYangOrder", "/api/qroauth/paiYangOrder", "派样下单", "1.0.0"),
		/** 出货后调用减货 (同时处理 成功及失败情况) */
		SHIPMENT_REPORTV2("016", "shipmentReportV2", "/api/goods/shipmentReportV2", "出货", "1.0.0"),
		/** 设置心跳 */
		SET_HEARTBEAT("017", "setHeartbeat", "/api/setHeartbeat", "设置心跳", "1.0.0")

		;

		private String code;
		private String serviceName;
		private String path;
		private String desc;
		private String version;

		public static ADPTE_METHOD selectAdpteByServiceNameAndVersion(String serviceName, String version) {
			for (ADPTE_METHOD method : ADPTE_METHOD.values()) {

				String innerServiceName = method.serviceName;
				String innerVersion = method.version;

				if (innerServiceName.equals(serviceName)) {
					if (!innerVersion.equals(version)) {
						return ADPTE_METHOD.ERROR_NO_VERSION;
					}
					return method;
				}
			}
			return ADPTE_METHOD.ERROR_NO_METHOD;
		}


		private ADPTE_METHOD(String code, String serviceName, String path, String desc, String version) {
			this.code = code;
			this.serviceName = serviceName;
			this.path = path;
			this.desc = desc;
			this.version = version;
		}

	}

	private String buildRequesParams(String json) {
		String params = "";
		if (StringUtils.isNotEmpty(json)) {
			JSONObject jsonObject = JSON.parseObject(json);
			Set<Entry<String, Object>> entrySet = jsonObject.entrySet();
			params += "?";
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				params += (key + "=" + value + "&");
			}
		}
		return params;
	}

}
