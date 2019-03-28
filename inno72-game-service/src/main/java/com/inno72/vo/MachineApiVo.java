package com.inno72.vo;

import java.util.List;

public class MachineApiVo {

	/**
	 * 商品特殊出货逻辑类型
	 */
	private String goodsLogic;
	/**
	 * 游戏ID
	 */
	private String gameId;
	/**
	 * 机器
	 */
	private String machineId;
	/**
	 * 货道号
	 */
	private String channelId;
	/**
	 * sessionUuid
	 */
	private String sessionUuid;
	/**
	 * 结果
	 */
	private String report;
	/**
	 * 没啥用
	 */
	private String userId;
	/**
	 * 商品ID
	 */
	private String itemId;

	/**
	 * 商品类型 0 商品； 1 优惠券
	 */
	private String itemType;
	/**
	 * 天猫订单号
	 */
	private String orderId;
	/**
	 * 活动ID
	 */
	private String activityId;

	/**
	 * 我方活动计划ID
	 */
	private String activityPlanId;
	/**
	 * 店铺ID
	 */
	private String shopId;
	/**
	 * 	安全ua
	 */
	private String ua;
	/**
	 * umid
	 */
	private String umid;
	/**
	 * 活动实例ID
	 */
	private String interactId;

	/**
	 * 失败货道货道号（逗号分隔）
	 */
	private String failChannelIds;

	/**
	 * 出货失败原因
	 */
	private List<String> describtion;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSessionUuid() {
		return sessionUuid;
	}

	public void setSessionUuid(String sessionUuid) {
		this.sessionUuid = sessionUuid;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getUmid() {
		return umid;
	}

	public void setUmid(String umid) {
		this.umid = umid;
	}

	public String getInteractId() {
		return interactId;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	public String getActivityPlanId() {
		return activityPlanId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public void setActivityPlanId(String activityPlanId) {
		this.activityPlanId = activityPlanId;
	}

	public String getFailChannelIds() {
		return failChannelIds;
	}

	public void setFailChannelIds(String failChannelIds) {
		this.failChannelIds = failChannelIds;
	}

	public List<String> getDescribtion() {
		return describtion;
	}

	public void setDescribtion(List<String> describtion) {
		this.describtion = describtion;
	}

	public String getGoodsLogic() {
		return goodsLogic;
	}

	public void setGoodsLogic(String goodsLogic) {
		this.goodsLogic = goodsLogic;
	}
}
