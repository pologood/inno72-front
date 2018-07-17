package com.inno72.vo;


import java.util.HashMap;
import java.util.Map;

import com.inno72.plugin.http.HttpClient;

/**
 * 主要用于给淘宝服务端上报机器日志使用
 *
 * 聚石塔请求路径  /api/top/addLog   ：logReqrest
 *
 */
public class LogReqrest {

	private static final long serialVersionUID = 7476676644484357165L;
	private String bizCode;//业务ID


	/**
	 *商品ID
	 */
	private Long itemId;

	/**
	 *  商家ID
	 */
	private Long sellerId;

	/**
	 *   业务类型
	 */
	private String type;
	public enum LogRequest_Type{

		machine_online_time("machine_online_time","开机时间"),
		lottery("machine_online_time","抽奖"),
		play_time("machine_online_time","单用户互动时长"),
		follow("machine_online_time","关注"),
		login("machine_online_time","登录用户"),
		;

		private String bizType;
		private String info;

		LogRequest_Type(String bizType, String info) {
			this.bizType = bizType;
			this.info = info;
		}

		public String getBizType() {
			return bizType;
		}

		public void setBizType(String bizType) {
			this.bizType = bizType;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}
	}


	/**
	 *  用户ID
	 */
	private Long userId;

	/**
	 *  机器ID
	 */
	private String value1;

	/**
	 *  买点字段
	 */
	private String value2;

	/**
	 *  长整型，根据埋点情况赋值
	 */
	private Long value3;

	/**
	 * 整数值，根据埋点填写
	 */
	private Long value4;

	public LogReqrest(String bizCode, Long itemId, Long sellerId, String type, Long userId, String value1,
			String value2, Long value3, Long value4) {
		this.bizCode = bizCode;
		this.itemId = itemId;
		this.sellerId = sellerId;
		this.type = type;
		this.userId = userId;
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
		this.value4 = value4;
	}

	public static String sendRequest(String accessToken, String url, LogReqrest reqrest){

		Map<String, String> requestForm = new HashMap<>();
		requestForm.put("accessToken", accessToken);
		return HttpClient.form(url + "/api/top/addLog", requestForm, null);

	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public Long getValue3() {
		return value3;
	}

	public void setValue3(Long value3) {
		this.value3 = value3;
	}

	public Long getValue4() {
		return value4;
	}

	public void setValue4(Long value4) {
		this.value4 = value4;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
