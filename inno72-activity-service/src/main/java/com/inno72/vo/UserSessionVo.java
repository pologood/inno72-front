package com.inno72.vo;

import java.util.List;

import lombok.Data;

@Data
public class UserSessionVo {

	private String mid;
	private String userNick;
	private String userId;
	private String accessToken;
	private String gameId;
	private String sessionUuid;
	private String mixnick;
	private String source;
	private String refOrderId;


	/**
	 * 查询游戏结果对应的货道时存入
	 */
	private String gameReport;
	/**
	 * 下单存入
	 */
	private String inno72OrderId;

	private String activityPlanId;

	private Long playTimes;
	private boolean canOrder;
	private String channelId;
	private String machineId;
	private String activityId;
	private String machineCode;
	private boolean countGoods;

	private int loginType = 0;

	private List<GoodsVo> goodsList;

	private boolean logged;

	public UserSessionVo(String mid, String userNick, String userId, String access_token, String gameId,
			String sessionUuid, String planId) {
		super();
		this.mid = mid;
		this.userNick = userNick;
		this.userId = userId;
		this.accessToken = access_token;
		this.gameId = gameId;
		this.sessionUuid = sessionUuid;
		this.activityPlanId = planId;
	}

	public UserSessionVo() {
		super();
	}


	public void setCanOrder(boolean canOrder) {
		this.canOrder = canOrder;
	}

	public boolean getCanOrder() {
		return canOrder;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setCountGoods(boolean countGoods) {
		this.countGoods = countGoods;
	}

	public boolean getCountGoods() {
		return countGoods;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public int getLoginType() {
		return loginType;
	}

}
