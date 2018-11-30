package com.inno72.vo;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.utils.StringUtil;

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
	public String getMachineCode(){
		return this.sessionUuid;

	}
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
	@Length(max = 6, min = 6, message = "非法类型")
	private String type;

	public Inno72MachineInformation build() {
		if (StringUtil.notEmpty(this.clientTime)){
			try {
				Long l = Long.parseLong(this.clientTime);
				this.clientTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date(l));
				this.requestId = StringUtil.uuid();
			}catch (Exception e){
				System.out.println(this.clientTime);
			}
		}
		return this;
	}

	public static enum ENUM_INNO72_MACHINE_INFORMATION_TYPE{
		LOGIN("001001","登录"),
		CONCERN("002001","关注"),
		MEMBERSHIP("003001","入会"),
		CLICK("004","点击"),// 客户端自由定义
		GAME_START("005001","游戏开始"),
		GAME_OVER("006001","游戏结束"),
		ORDER_GOODS("007001","下单-商品"),
		ORDER_COUPON("007002","下单-优惠券"),
		SHIPMENT("008001","出货"),
		LOCK_CHANNEL("008002","锁货道"),
		SCAN_LOGIN("009001","扫码"),
		SCAN_PAY("009002","扫码"),
		JUMP("010001","跳转"),
		PAY("011002","订单支付"),
		PRODUCT_CLICK("100100","商品点击"),
		;
		private String type;
		private String desc;

		ENUM_INNO72_MACHINE_INFORMATION_TYPE(String type, String desc) {
			this.type = type;
			this.desc = desc;
		}

		public String getType() {
			return type;
		}

		public String getDesc() {
			return desc;
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

	/** 失败货道 */
	private String failChannelIds;

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

	/** 计划ID */
	private String planId;
	/** 奖池ID */
	private String interactId;

	private String clickType;

	private String requestId;


	/**
	 * 商户总ID - table -> inno72_merchant_user.merchant_id
	 */
	private String merchantId;// TODO 新

	/**
	 * 商户总名称 - table -> inno72_merchant_user.merchant_name
	 */
	private String merchantName;// TODO 新

	/**
	 * 渠道商家ID - table -> inno72_merchant.id
	 */
	private String channelMerchantId;//TODO 新
	/**
	 * 渠道ID - table -> inno72_merchant.channel_id
	 */
	private String channelId;//TODO 新
	/**
	 * 渠道名称 - table -> inno72_merchant.channel_name
	 */
	private String channelName;//TODO 新

	public Inno72MachineInformation(String type, String sessionUuid) {
		this.sessionUuid = sessionUuid;
		this.type = type;
	}

	public Inno72MachineInformation() {
	}
}
