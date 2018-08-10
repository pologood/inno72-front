package com.inno72.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonBean;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72FileService;
import com.inno72.vo.UserSessionVo;

@Service
public class Inno72FileServiceImpl implements Inno72FileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72FileServiceImpl.class);

	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

	@Override
	public Result<Object> skindetect(String sessionUUid, String picBase64) {

		LOGGER.info("肌肤检测接口 -- 参数 sessionUUid=>({}); picBase64 => ({})", sessionUUid, picBase64);
		if ( StringUtil.isEmpty(sessionUUid) || StringUtil.isEmpty(picBase64) ){
			LOGGER.info("参数缺失.");
			return Results.failure("参数缺失!");
		}

		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUUid);

		if ( sessionKey == null ){
			return Results.failure("登录超时!");
		}

		String jstUrl = inno72GameServiceProperties.get("jstUrl");

		try {

			String requestUrl = jstUrl + "/api/top/" + sessionKey.getAccessToken();
			Map<String, String> requestParams = new HashMap<>();
			requestParams.put("image", CommonBean.PIC_BASE64_START_WITH + picBase64);
			requestParams.put("mixnick",sessionKey.getUserNick());
			requestParams.put("source",sessionKey.getSource());

			String respJson = HttpClient.form(requestUrl, requestParams, null);
			if ( StringUtil.isEmpty(respJson) ){
				LOGGER.info("调用聚石塔 肌肤检测接口返回空 url => {}", requestUrl, JSON.toJSONString(requestParams));
				return Results.failure("服务调用失败!");
			}
//			{"tmall_marketing_face_skindetect_response":{"detect_result":"{\"code\":200,\"msg\":\"success\",\"data\":{\"detect_time\":1533884040709,\"msg\":\"image not clear\",\"age\":23,\"gender\":0,\"color_level\":-1,\"hue_level\":1,\"oil_cheeck\":-1,\"oil_t_area\":-1,\"oil_chin\":-1,\"oil_level\":-1,\"smooth_level\":-1,\"acne_level\":-1,\"pore_level\":-1,\"blackheads\":0,\"black_level\":-1,\"acne_loc\":[],\"r_face\":[110,155,263,157],\"code\":4}}","request_id":"148olt4srz3mn"}}
			String data = FastJsonUtils.getString("data", respJson);
			return Results.success(JSON.parseObject(data));
		}catch (Exception e){
			LOGGER.info("调用聚石塔 肌肤检测接口失败 ");
		}

		return Results.failure("服务调用失败!");
	}
}
