package com.inno72.vo;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotNull;

import com.inno72.common.datetime.LocalDateTimeUtil;

import lombok.Data;

/**
 * 好爸爸定制活动 保存用户信息-> 9月10日后 公布抽奖信息使用
 */
@Data
public class KispaLotteryBean {

	/**
	 * 微博ID
	 */
	@NotNull(message = "微博ID为空!")
	private String weiboId;
	/**
	 * 淘宝ID
	 */
	@NotNull(message = "淘宝ID为空!")
	private String taobaoId;
	/**
	 * 姓名
	 */
	@NotNull(message = "姓名为空!")
	private String name;
	/**
	 * 电话
	 */
	@NotNull(message = "电话为空!")
	private String phone;
	/**
	 * 创建时间
	 */
	private String createTime;


	/**
	 * 验证CODE
	 */
	@NotNull(message = "Code为空!")
	private String authCode;
	/**
	 * 登录的sessionUuid
	 */
	private String sessuonUuid;

	public KispaLotteryBean(String weiboId, String taobaoId, String name, String phone, String authCode,
			String sessuonUuid) {
		this.weiboId = weiboId;
		this.taobaoId = taobaoId;
		this.name = name;
		this.phone = phone;
		this.authCode = authCode;
		this.sessuonUuid = sessuonUuid;
		this.createTime = LocalDateTimeUtil
				.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public KispaLotteryBean() {
	}
}
