package com.inno72.vo;

import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotNull;

import com.inno72.common.datetime.LocalDateTimeUtil;

import lombok.Data;

@Data
public class RequestMachineInfoVo {

	/** sessionUuid  */
	@NotNull(message = "不能为空!")
	private String sessionUuid;

	/** 客户端请求时间 2018/11/1 15:10 */
	@NotNull(message = "时间不能为空!")
	private String clientTime;

	/** 动作类型(遵循@Inno72MachineInformationtype) 1001 */
	@NotNull(message = "动作类型不能为空!")
	private String type;

	/** 游戏难度 1 */
	private String playDifficulty;

	/** 游戏结果 70/成功 */
	private String playResult;

	/** 页面编码  */
	@NotNull(message = "页面编码不能为空!")
	private String pageCode;

	/** 页面名称  */
	@NotNull(message = "页面名称不能为空!")
	private String pageName;

	@NotNull(message = "计划不能为空!")
	private String planId;

	/** 自动手动，默认是自动.页面跳转行为使用 0/1 */
	private String clickType;

	public String getClientTime(){
		try {
			long l = Long.parseLong(this.clientTime);
			return  LocalDateTimeUtil.transfer(LocalDateTimeUtil.long2LocalDateTime(l), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
		}catch (Exception e){
			return this.clientTime;
		}
	}
}
