package com.inno72.msg.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.plugin.http.HttpClient;

public class QyHandler {

	/**
	 * 根据userId获取OpenId
	 * 
	 * @param userId
	 * @return
	 */
	public static String getOpenId(String userId, String token) {
		String url = MessageFormat.format(CommonConstants.QY_GETOPENID, token);
		Map<String, String> map = new HashMap<>();
		map.put("userid", userId);
		String result = HttpClient.post(url, JSON.toJSONString(map));
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getInteger("errcode") == 0) {
			return resultJson.getString("openid");
		}
		return "";
	}
}
