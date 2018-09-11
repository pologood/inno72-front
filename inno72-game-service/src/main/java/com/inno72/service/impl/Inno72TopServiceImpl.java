package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72TopService;
import com.inno72.vo.LogReqrest;
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

	@Override
	public void fllowshopLog(String sessionUuid, String sellerId) {
		LOGGER.info("fllowshopLog sessionUuid is {}", sessionUuid, sellerId);
		LogReqrest logReqrest = new LogReqrest();
		logReqrest.setType(LogReqrest.LogRequest_Type.follow.getBizType());
		logReqrest.setSellerId(Long.valueOf(sellerId));
		this.addLog(sessionUuid, logReqrest);
	}

	@Override
	public void lotteryLog(String sessionUuid, String interactId, String sellerId) {
		LOGGER.info("lotteryLog sessionUuid is {}, interactId is {}, sellerId is {}", sessionUuid, interactId, sellerId);
		LogReqrest logReqrest = new LogReqrest();
		logReqrest.setType(LogReqrest.LogRequest_Type.lottery.getBizType());
		logReqrest.setValue2(interactId);
		logReqrest.setSellerId(Long.valueOf(sellerId));
		this.addLog(sessionUuid, logReqrest);
	}

	@Override
	public Result lottory(String sessionUuid, String ua, String umid, String interactId, String shopId) {
		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		UserSessionVo sessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", sessionVo.getAccessToken());
		requestForm.put("ua", ua); // 安全ua
		requestForm.put("umid", umid);// umid
		requestForm.put("interactId", interactId);// 互动实例ID
		requestForm.put("shopId", shopId);// 店铺id

		String requestUrl = jstUrl + "/api/top/lottory";

		LOGGER.info("请求聚石塔抽奖接口参数 {}", JSON.toJSONString(requestForm));
		String respJson = "";
		try {
			respJson = HttpClient.form(requestUrl, requestForm, null);
			LOGGER.info("请求聚石塔抽奖接口返回结果 {}", respJson);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Results.failure("请求聚石塔抽奖接口报错");
		}

		if (StringUtil.isEmpty(respJson)) {
			return Results.failure("聚石塔无返回数据!");
		}
		return Results.success(respJson);
	}

	@Override
	public String getMaskUserNick(String sessionUuid, String sellerId) {
		LOGGER.info("getMaskUserNick params sessionUuid is {}, sellerId is {}", sessionUuid, sellerId);
		String nickName = "";
		String respJson = "";
		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		UserSessionVo sessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", sessionVo.getAccessToken());
		requestForm.put("mid", sessionVo.getMachineCode());
		requestForm.put("sellerId", sellerId);
		requestForm.put("mixNick", sessionVo.getUserId()); // 实际为taobao_user_nick

		try {
			LOGGER.info("getMaskUserNick params is {}", JsonUtil.toJson(requestForm));
			respJson = HttpClient.form(jstUrl + "/api/top/getMaskUserNick", requestForm, null);
			LOGGER.info("调用聚石塔接口 getMaskUserNick 返回 {}", JSON.toJSONString(respJson));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		if (StringUtil.isNotEmpty(respJson)) {
			nickName = FastJsonUtils.getString(respJson, "model");
		}
		return nickName;
	}

	private void addLog(String sessionUuid, LogReqrest reqrest) {
		LOGGER.info("addLog sessionUuid is {}, reqrest is {} ", sessionUuid, JsonUtil.toJson(reqrest));
		UserSessionVo sessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (sessionVo == null) {
			LOGGER.warn("addLog sessionVo 为空!");
			return;
		}
		String accessToken = sessionVo.getAccessToken();
		String machineCode = sessionVo.getMachineCode();

		String jstUrl = inno72GameServiceProperties.get("jstUrl");
		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("value1", machineCode); // 机器code
		requestForm.put("value2", reqrest.getValue2());
		requestForm.put("sellerId", reqrest.getSellerId().toString());
		requestForm.put("type", reqrest.getType());
		requestForm.put("accessToken", accessToken);
		requestForm.put("mixnick", sessionVo.getUserId());

		try {
			LOGGER.info("addLog params is {}", JsonUtil.toJson(requestForm));
			String result = HttpClient.form(jstUrl + "/api/top/addLog", requestForm, null);
			LOGGER.info("addLog result is {}", result);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
