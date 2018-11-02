package com.inno72.vo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Inno72MachineInformation {


	/** sessionUuid */
	@NotNull(message = "登录信息不存在!")
	private String sessionUuid;
	/** traceId */
	private String traceId;

	/** 活动ID */
	private String activityId;

	/** 活动名称 */
	private String activityName;

	/** 机器ID */
	private String machineCode;

	/** 省 */
	private String provence;

	/** 市 */
	private String city;

	/** 区 */
	private String district;

	/** 点位 */
	private String point;

	/** userID */
	private String userId;

	/** 用户昵称 */
	private String nickName;

	/** 时间1(客户端传入时间) */
	private String clientTime;

	/** 时间2(到达服务器时间) */
	private String serviceTime;

	/** 行为 - (登录:  、关注:002 、入会:003  、、、、 */
	@NotNull(message = "消息类型不能为空!")
	private String type;

	public static enum ENUM_INNO72_MACHINE_INFORMATION_TYPE{
		LOGIN("001","登录"),
		CONCERN("002 ","关注"),
		MEMBERSHIP("003 ","入会"),
		CLICK("004 ","点击"),
		GAME_START("005 ","游戏开始"),
		GAME_OVER("006 ","游戏结束"),
		ORDER("007 ","下单"),
		SHIPMENT("008 ","出货"),
		SCAN("009 ","扫码"),
		JUMP("010 ","跳转"),

		;
		private String type;
		private String desc;

		ENUM_INNO72_MACHINE_INFORMATION_TYPE(String type, String desc) {
			this.type = type;
			this.desc = desc;
		}
	}

	/** 品牌ID(seller_id) */
	private String sellerId;

	/** 品牌名称 */
	private String sellerName;

	/** 商品ID(商品code) */
	private String goodsId;

	/** 商品名称 */
	private String goodsName;

	/** playCode */
	private String playCode;

	/** 游戏难度 */
	private String playDifficulty;

	/** 游戏结果 */
	private String playResult;

	/** 订单号  */
	private String orderId;

	/** 三方订单号 */
	private String refOrderId;

	/** 三方订单状态 */
	private String refOrderStatus;

	/** 货道 */
	private String channel;

	/** 出货数量 */
	private String shipmentNum;

	/** 描述 ex: 点击进入游戏 or 跳转位置 等等 */

	private String desc;

	/** 扫码路径 ex:https://。。。。。 */

	private String scanUrl;

	/** pageCode */
	private String pageCode;

	/** pageName */
	private String pageName;
}
