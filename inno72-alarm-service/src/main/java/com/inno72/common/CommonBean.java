package com.inno72.common;

public class CommonBean {


	/**
	 * game session key
	 *
	 * machine_plan:
	 */
	public static final String REDIS_ACTIVITY_PLAN_CACHE_KEY = "machine_plan:";
	public static final int REDIS_ACTIVITY_PLAN_CACHE_EX_KEY = 12 * 60 * 60;

	/**
	 * 活动计划 登录次数统计 redis - set
	 */
	public static final String REDIS_ACTIVITY_PLAN_LOGIN_TIMES_KEY = "game_service:activity_plan:login_times:";


}
