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

		LOGGER.debug("inno72 开放接口 request 参数 ==> requestJson -> {} ", requestJson);
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

		/** 没有方法 */
		ERROR_NO_METHOD("404", "ERROR_NO_METHOD", "/inno72/noMethod/open", "出货后调用减货", "1.0.0"),
		/** 版本不存在 */
		ERROR_NO_VERSION("500", "ERROR_NO_VERSION", "/inno72/noVersion/open", "出货后调用减货",
				"1.0.0"),
		/** 货道异常信息存储 */
		/** 掉货失败 */
		/* 用户互动时长 */
		GET_SAMPLING("012", "getSampling", "/api/getSampling", "获取派样商品", "1.0.0"),
		/* 获取派样商品 */
		/** 创建派样订单 */
		/** 出货后调用减货 (同时处理 成功及失败情况) */
		/** 设置心跳 */
		SET_HEARTBEAT("017", "setHeartbeat", "/api/setHeartbeat", "设置心跳", "1.0.0"),
		/* 获取派样商品 */
		GET_SAMPLINGNEW("018", "getSamplingNew", "/api/getSamplingNew", "获取派样商品", "1.0.0"),
		/**
		 * 登陆回调
		 */
		GET_PROCESSBEFORELOGGED("019", "processBeforeLogged", "/api/standard/processBeforeLogged", "登陆回调", "1.0.0"),

		/** 查找活动商品 */
		ACTIVITY_GOODS("020", "findGoods", "/api/activity/findGoods", "查找活动商品接口", "1.0.0"),

		CHECKPHONEVERIFICATIONCODE("022", "checkPhoneVerificationCode", "/api/unstandard/checkPhoneVerificationCode", "校验验证码", "1.0.0"),
		GETPHONEVERIFICATIONCODE("023", "getPhoneVerificationCode", "/api/unstandard/getPhoneVerificationCode", "获取验证码", "1.0.0"),
		UPFILE("024", "upfile", "/api/unstandard/upfile", "上传拍照图片", "1.0.0"),
		CHANGEPAYTYPE("025", "changePayType", "/api/unstandard/changePayType", "修改支付方式", "1.0.0"),
		GAMEPOINTTIME("026", "gamePointTime", "/api/unstandard/gamePointTime", "记录游戏时间点", "1.0.0"),
		REFUNDASK("027", "refundAsk", "/api/unstandard/refundAsk", "申请退款", "1.0.0"),

		BUILDWEIDAQRCODE("027", "buildWeiDaQrCode", "/api/unstandard/buildWeiDaQrCode", "创建维达二维码", "1.0.0"),

		WEIDASCANFLAGPOLLING("028", "weidaScanFlagPolling", "/api/unstandard/weidaScanFlagPolling", "获取维达二维码扫描状态", "1.0.0"),

		WEIDAREDIRECT("029", "weidaRedirect", "/api/unstandard/weidaRedirect", "跳转到维达页面", "1.0.0"),

		saveWeidaScanLog("030", "saveWeidaScanLog", "/api/unstandard/saveWeidaScanLog", "保存维达扫描日志", "1.0.0"),

		STANDARD_PREPARELOGIN("100", "standardPrepareLogin", "/api/standard/prepareLogin", "预登陆", "1.0.0"),
		
		STANDARD_REDIRECTLOGIN("101", "standardRedirectLogin", "/api/standard/redirectLogin", "跳转登陆", "1.0.0"),
		
		STANDARD_ORDER("102", "standardOrder", "/api/standard/order", "下单", "1.0.0"),
		
		STANDARD_SHIPMENT("103", "standardShipment", "/api/standard/shipment", "出货", "1.0.0"),

		/** 获取登录信息 */
		SESSION_POLLING_TANDARD("104", "standardSessionPolling", "/api/standard/sessionPolling", "获取登录信息", "1.0.0"),

		/** 标准下单接口 */
		ORDER_STANDARD("105", "standardOrder", "/api/standard/order", "标准下单接口", "1.0.0"),

		/** 标准订单polling接口 */
		ORDER_POLLING_STANDARD("106", "standardOrderPolling", "/api/standard/orderPolling", "标准订单polling接口", "1.0.0"),

		/** 标准查找活动接口 */
		FINDACTIVITY_STANDARD("107", "standardFindActivity", "/api/standard/findActivity", "标准订单polling接口", "1.0.0"),

		/** 标准查找活动接口APP */
		FINDACTIVITY_STANDARD_APP("109", "standardActivityCodeForApp", "/api/standard/findActivityForApp", "标准获取游戏code接口", "1.0.0"),

		/** 标准查找活动接口 */
		POINT_LOG("108", "point", "/api/point", "标准埋点接口", "1.0.0"),

		/** 标准查找活动接口 */
		STANDARD_LOTTERY("110", "standardLottery", "/api/standard/lottery", "发送优惠券", "1.0.0"),
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


		ADPTE_METHOD(String code, String serviceName, String path, String desc, String version) {
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
