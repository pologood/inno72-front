package com.inno72.common;

public class CommonConstants {

	/** 钉钉消息加密用的token **/
	public final static String DD_TOKEN = "123456";
	/** 钉钉消息加密用的key **/
	public final static String AES_KEY = "1111111111111111111111111111111111111111111";
	/** 钉钉企业id **/
	public final static String DD_CORPID = "dingd04d2d6ca18d0fd535c2f4657eb6378f";

	/** 用户登录信息_缓存KEY前缀 **/
	public static final String USER_LOGIN_CACHE_KEY_PREF = "machine-check-app-backend:login_user:";

	/** 用户登录TOKEN_缓存KEY前缀 **/
	public static final String USER_LOGIN_TOKEN_CACHE_KEY_PREF = "machine-check-app-backend:login_user_token:";

	/** 被踢出用户集合 **/
	public static final String CHECK_OUT_USER_TOKEN_SET_KEY = "machine-check-app-backend:checkout_user_token_set:";

	public static final String CHECK_USER_SMS_CODE_KEY_PREF = "machine-check-app-backend:sms_code:";

	public static final String SUPPLY_CHANNEL_LACK_GOODS_PREF = "machine-check-app-backend:lack_goods:";

	/** 用户sessionData有效期 **/
	public static final int SESSION_DATA_EXP = 3600 * 24*100;

	/** 用户登录对象 **/
	public static SessionData SESSION_DATA = new SessionData();

	/** OSS基础路径 **/
	public static final String OSS_PATH = "backend";
	/** OSS基础路径 **/
	public static final String ALI_OSS = "https://inno72.oss-cn-beijing.aliyuncs.com/";

	/**验证码key前缀*/
	public static final String SMS_CODE_KEY = "MACHINE_APP_BACKEND_";

	/**
	 *APP下载地址前缀
	 */
	public static final String DOWNLOAD_APP_PREF = "http://inno72.oss.72solo.com";

	/**日志：入厂*/
	public static final String LOG_TYPE_IN_FACTORY = "41";

	/**日志：机器打卡*/
	public static final String LOG_TYPE_MACHINE_SIGN = "42";

	/**日志：机器补货*/
	public static final String LOG_TYPE_MACHINE_SUPPLY = "43";

	/**日志：处理工单*/
	public static final String LOG_TYPE_SET_WORK = "44";

	/**日志：拆分货道*/
	public static final String LOG_TYPE_SPLIT_CHANNEL = "45";

	/**日志：合并货道*/
	public static final String LOG_TYPE_MERGE_CHANNEL = "46";

}
