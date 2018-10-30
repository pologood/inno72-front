package com.inno72.vo;

import com.inno72.model.Inno72ActivityPlan;
import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72Machine;

public class Inno72MachineVo extends Inno72Machine {
	/**
	 * 活动类型，1派样，0普通
	 */
	private Integer activityType;

	public final static Integer ACTIVITYTYPE_PAIYANG=1;
	public final static Integer ACTIVITYTYPE_NOTPAIYANG=0;
	/**
	 * 为true时，客户端重新加载游戏
	 */
	private boolean isReload;

	/**
	 * 机器绑定的游戏
	 */
	private Inno72Game inno72Games;

	/**
	 * 商户名称
	 */
	private String brandName;
	/**
	 * 商户名称
	 */
	private String planCode;

	/**
	 * 活动计划ID
	 */
	private String activityPlanId;
	/**
	 * 活动ID
	 */
	private String activityId;
	/**
	 * 渠道ID
	 */
	private String channelId;

	/**
	 * 活动计划详情
	 */
	private Inno72ActivityPlan inno72ActivityPlan;
	/**
	 * 0普通派样1新零售派样
	 */
	private Integer paiyangType;

	/**
	 * 奖品类型（100100商品，100200优惠券，100300商品+优惠券)
	 */
	private String prizeType;

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public boolean isReload() {
		return isReload;
	}

	public void setReload(boolean isReload) {
		this.isReload = isReload;
	}

	public Inno72Game getInno72Games() {
		return inno72Games;
	}

	public void setInno72Games(Inno72Game inno72Games) {
		this.inno72Games = inno72Games;
	}

	public String getActivityPlanId() {
		return activityPlanId;
	}

	public void setActivityPlanId(String activityPlanId) {
		this.activityPlanId = activityPlanId;
	}

	public void setInno72ActivityPlan(Inno72ActivityPlan inno72ActivityPlan) {
		this.inno72ActivityPlan = inno72ActivityPlan;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Inno72ActivityPlan getInno72ActivityPlan() {
		return inno72ActivityPlan;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public Integer getPaiyangType() {
		return paiyangType;
	}

	public void setPaiyangType(Integer paiyangType) {
		this.paiyangType = paiyangType;
	}
}