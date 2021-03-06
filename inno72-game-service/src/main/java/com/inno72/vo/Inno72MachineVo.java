package com.inno72.vo;

import com.inno72.model.Inno72ActivityPlan;
import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72Machine;

import lombok.Data;

@Data
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

	private String provence;

	/** playCode */
	private String playCode;
	/** 市 */
	private String city;

	/** 区 */
	private String district;

	/** 点位 */
	private String point;
	/**
	 * 活动名称
	 */
	private String activityName;

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

}