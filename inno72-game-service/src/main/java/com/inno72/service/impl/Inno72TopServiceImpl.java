package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72TopService;
import com.inno72.vo.UserSessionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class Inno72TopServiceImpl implements Inno72TopService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72TopServiceImpl.class);

	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

	public String jstUrl = inno72GameServiceProperties.get("jstUrl");

	@Override
	public String order(String sessionUuid, String activityId, String itemId) {

		UserSessionVo userSessionVo = this.getUserSessionVo(sessionUuid);

		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", userSessionVo.getAccessToken());
		requestForm.put("activityId", activityId);
		requestForm.put("mid", userSessionVo.getMachineCode()); // 实际为code
		requestForm.put("goodsId", itemId);

		String respJson = "";
		try {
			LOGGER.info("调用聚石塔下单接口参数{}", JSON.toJSONString(requestForm));
			respJson = HttpClient.form(jstUrl + "/api/top/order", requestForm, null);
		} catch (Exception e) {
			LOGGER.error("调用聚石塔order接口失败 ! {}", e.getMessage(), e);
		}
		return respJson;
	}

	@Override
	public String orderPolling(String sessionUuid, String orderId) {
		String respJson = "";
		UserSessionVo userSessionVo = this.getUserSessionVo(sessionUuid);
		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", userSessionVo.getAccessToken());
		requestForm.put("orderId", orderId);
		try {
			respJson = HttpClient.form(jstUrl + "/api/top/order-polling", requestForm, null);
		} catch (Exception e) {
			LOGGER.error("调用聚石塔order-polling接口失败 ! {}", e.getMessage(), e);
		}
		LOGGER.info("调用聚石塔接口 order-polling 返回 {}", JSON.toJSONString(respJson));
		return respJson;
	}

	@Override
	public String lottory(String sessionUuid, String ua, String umid, String interactId, String shopId) {
		String respJson = "";
		UserSessionVo userSessionVo = this.getUserSessionVo(sessionUuid);

		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", userSessionVo.getAccessToken());
		requestForm.put("ua", ua); // 安全ua
		requestForm.put("umid", umid);// umid
		requestForm.put("interactId", interactId);// 互动实例ID
		requestForm.put("shopId", shopId);// 店铺ID

		String requestUrl = jstUrl + "/api/top/lottory";

		LOGGER.info("请求聚石塔 url, 参数 {}", requestUrl, JSON.toJSONString(requestForm));
		try {
			respJson = HttpClient.form(requestUrl, requestForm, null);
			LOGGER.info("调用聚石塔接口lottory返回 {}", respJson);
		} catch (Exception e) {
			LOGGER.error("调用聚石塔lottory接口失败 ! {}", e.getMessage(), e);
		}
		return respJson;
	}

	@Override
	public void deliveryRecord(String sessionUuid, String channelId) {
		UserSessionVo userSessionVo = this.getUserSessionVo(sessionUuid);

		Map<String, String> requestForm = new HashMap<>();

		requestForm.put("accessToken", userSessionVo.getAccessToken());
		requestForm.put("orderId", userSessionVo.getRefOrderId());
		requestForm.put("mid", userSessionVo.getMachineCode());
		requestForm.put("channelId", channelId);// 互动实例ID

		String respJson = "";
		try {
			respJson = HttpClient.form(inno72GameServiceProperties.get("jstUrl") + "/api/top/deliveryRecord",
					requestForm, null);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("调用聚石塔接口deliveryRecord返回 {}", JSON.toJSONString(respJson));

		String msg_code = FastJsonUtils.getString(respJson, "msg_code");
		if (!msg_code.equals("SUCCESS")) {
			LOGGER.info("返回非成功状态，不能更细订单状态为成功 {}", respJson);
		}
	}

	private UserSessionVo getUserSessionVo(String sessionUuid) {
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		return userSessionVo;
	}
}
