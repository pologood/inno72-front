package com.inno72.vo;

import com.inno72.model.Inno72ActivityPlan;
import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72Machine;

public class Inno72MachineVo extends Inno72Machine {

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
	 * 活动计划ID
	 */
	private String activityPlanId;

	/**
	 * 活动计划详情
	 */
	private Inno72ActivityPlan inno72ActivityPlan;


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

	public Inno72ActivityPlan getInno72ActivityPlan() {
		return inno72ActivityPlan;
	}

	public void setInno72ActivityPlan(Inno72ActivityPlan inno72ActivityPlan) {
		this.inno72ActivityPlan = inno72ActivityPlan;
	}
}
