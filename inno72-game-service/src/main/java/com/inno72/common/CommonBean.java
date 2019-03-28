package com.inno72.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.log.PointLogContext;
import com.inno72.log.vo.LogType;

@Component
public class CommonBean {

	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;
	private static String jstUtl = "";

	@PostConstruct
	public void initClient() {
		jstUtl = inno72GameServiceProperties.get("jstUrl");
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonBean.class);
	/**
	 * game session key
	 *
	 * machine_plan:
	 */
	public static final String REDIS_ACTIVITY_PLAN_CACHE_KEY = "machine_plan:";
	public static final String REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_KEY = "machine_default_plan:";
	public static final int REDIS_ACTIVITY_PLAN_CACHE_EX_KEY = 12 * 60 * 60;
	public static final int REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_EX_KEY = 14 * 12 * 60 * 60;

	/**
	 * 活动计划 登录次数统计 redis - set
	 */
	public static final String REDIS_ACTIVITY_PLAN_LOGIN_TIMES_KEY = "game_service:activity_plan:login_times:";


	/**
	 * 立顿活动 redis 高价商品出货率, 整数 百分之80填写为 80，
	 * 计算规则：如果单个机器当次可以出货高价商品，概率计算后没有出货则增加百分之10，最高百分之70，如果概率计算后出货高价商品则调整概率为最低百分之10
	 */
	public static final String REDIS_ACTIVITY_LIDUN_GOODS_PROBABILITY_KEY = "game_service:activity_plan:goods:lidun:probability";
	public static final String REDIS_ACTIVITY_LIDUN_GOODS_SHIPMENT_INDEX_KEY = "game_service:activity_plan:goods:lidun:shipment_index";


	/**
	 * 商品特殊出货逻辑类型
	 */
	public static class goodsLogic{
		//立顿
		public static final String LI_DUN = "LI_DUN";
	}



	/**
	 * 护肤检测传入base64格式 以base64,开始
	 */
	public static final String PIC_BASE64_START_WITH = "base64,";

	/* ************************************************ -> 埋点 <- ***************************************************** */

	// 登录
	public static final String POINT_TYPE_LOGIN = "31";
	// 商品下单
	public static final String POINT_TYPE_GOODS_ORDER = "32";
	// 优惠券下单
	public static final String POINT_TYPE_COUPON_ORDER = "35";
	// 出货量
	public static final String POINT_TYPE_FINISH = "33";
	// 客流量 外部传入
	public static final String POINT_TYPE_FANS = "34";
	// 关注店铺数量 外部传入
	public static final String POINT_TYPE_CONCERN = "36";

	/**
	 * @param msg 消息体
	 *            msg[0] type 日志类型
	 *            msg[1] machineCode 机器code
	 *            msg[2] detail 详情
	 */
	public static void logger(String ... msg){
		new PointLogContext(LogType.POINT)
				.machineCode(msg[1])
				.pointTime(LocalDateTimeUtil.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.type(msg[0])
				.detail(msg[2])
				.tag(msg[3]).bulid();
		LOGGER.info("记录埋点数据 [{}]", JSON.toJSONString(msg));
	}
	/* ************************************************ -> 埋点 <- ***************************************************** */

	/** 聚石塔接口 */
	public static class TopUrl{
		/** 关注 */
		public static final String CONCERN = jstUtl + "/api/top/concern/";
		/** 下单 */
		public static final String ORDER = jstUtl + "/api/top/order/";
		/** 获取nick name  */
		public static final String NICK = jstUtl + "/api/top/getMaskUserNick/";
		/** 出货 */
		public static final String DELIVERY_RECORD = jstUtl + "/api/top/deliveryRecord/";
		/** 获取订单状态 */
		public static final String ORDER_POLLING = jstUtl + "/api/top/order-polling/";
		/** 抽奖 */
		public static final String LOTTORY = jstUtl + "/api/top/lottory/";
	}

	public static String getActive() {
		String active = System.getenv("spring_profiles_active");
		LOGGER.info("获取spring_profiles_active：{}", active);
		if (active == null || active.equals("")) {
			LOGGER.info("未读取到spring_profiles_active的环境变量,使用默认值: dev");
			active = "dev";
		}
		return active;
	}
}
