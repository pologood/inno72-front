package com.inno72.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;
import com.inno72.common.LocalDateConverter;

@Table(name = "inno72_order")
public class Inno72Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
	private String id;

	@Column(name = "order_num")
	private String orderNum;

	/**
	 * inno72_game_user.id
	 */
	@Column(name = "user_id")
	private String userId;

	/**
	 * inn72_channel.id
	 */
	@Column(name = "channel_id")
	private String channelId;

	/**
	 * inno72_machine.id
	 */
	@Column(name = "machine_id")
	private String machineId;

	/**
	 * 店铺ID
	 */
	@Column(name = "shops_id")
	private String shopsId;

	/**
	 * 店铺ID
	 */
	@Column(name = "shops_name")
	private String shopsName;

	@Column(name = "merchant_id")
	private String merchantId;

	/**
	 * 	活动ID
	 */
	@Column(name = "inno72_activity_id")
	private String inno72ActivityId;

	/**
	 * 活动计划ID
	 */
	@Column(name = "inno72_activity_plan_id")
	private String inno72ActivityPlanId;

	/**
	 * 下单时间
	 */
	@Column(name = "order_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateConverter.class)
	private LocalDateTime orderTime;

	/**
	 * 订单金额
	 */
	@Column(name = "order_price")
	private BigDecimal orderPrice;

	@Column(name = "pay_price")
	private BigDecimal payPrice;
	@Column(name = "pay_type")
	private Integer payType;
	/**
	 * 订单类型 10
	 */
	@Column(name = "order_type")
	private Integer orderType;

	public enum INNO72ORDER_PAYTYPE {

		WECHAT(1, "微信"),ALIPAY(2, "支付宝");

		private Integer key;
		private String desc;

		INNO72ORDER_PAYTYPE(Integer key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	public enum INNO72ORDER_ORDERTYPE {

		DEFAULT(999, "默认");

		private Integer key;
		private String desc;

		INNO72ORDER_ORDERTYPE(Integer key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	@Column(name = "pay_status")
	private Integer payStatus;

	@Column(name = "order_status")
	private Integer orderStatus;

	public enum INNO72ORDER_PAYSTATUS {

		SUCC(1, "已支付"), WAIT(0, "未支付");

		private Integer key;
		private String desc;

		INNO72ORDER_PAYSTATUS(Integer key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	public enum INNO72ORDER_ORDERSTATUS {

		WAIT(10, "未支付"),PAY(20, "已支付"),COMPLETE(30, "已完成"),REFUND(30, "已退款");

		private Integer key;
		private String desc;

		INNO72ORDER_ORDERSTATUS(Integer key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	/**
	 * 商品类型
	 */
	@Column(name = "goods_type")
	private Integer goodsType;

	public enum INNO72ORDER_GOODSTYPE {

		PRODUCT(1, "商品"), COUPON(2, "优惠券");

		private Integer key;
		private String desc;

		INNO72ORDER_GOODSTYPE(Integer key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	@Column(name = "goods_status")
	private Integer goodsStatus;

	public enum INNO72ORDER_GOODSSTATUS {

		SUCC(1, "出货成功"), WAIT(0, "未出货");

		private Integer key;
		private String desc;

		INNO72ORDER_GOODSSTATUS(Integer key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	@Column(name = "pay_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateConverter.class)
	private LocalDateTime payTime;

	@Column(name = "ref_order_status")
	private String refOrderStatus;

	/**
	 * 三方OrderId
	 */
	@Column(name = "ref_order_id")
	private String refOrderId;

	/**
	 * 是否为重复单（0:不是，1:是）
	 */
	private Integer repetition;

	public enum INNO72ORDER_REPETITION {

		NOT(1, "不重复"), REPETITION(0, "重复单");

		private Integer key;
		private String desc;

		INNO72ORDER_REPETITION(Integer key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}

	/**
	 * @return order_num
	 */
	public String getOrderNum() {
		return orderNum;
	}

	/**
	 * @param orderNum
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 获取inno72_game_user.id
	 *
	 * @return user_id - inno72_game_user.id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置inno72_game_user.id
	 *
	 * @param userId inno72_game_user.id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取inn72_channel.id
	 *
	 * @return channel_id - inn72_channel.id
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * 设置inn72_channel.id
	 *
	 * @param channelId inn72_channel.id
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * 获取inno72_machine.id
	 *
	 * @return machine_id - inno72_machine.id
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * 设置inno72_machine.id
	 *
	 * @param machineId inno72_machine.id
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * 获取店铺ID
	 *
	 * @return shops_id - 店铺ID
	 */
	public String getShopsId() {
		return shopsId;
	}

	/**
	 * 设置店铺ID
	 *
	 * @param shopsId 店铺ID
	 */
	public void setShopsId(String shopsId) {
		this.shopsId = shopsId;
	}

	/**
	 * 获取店铺ID
	 *
	 * @return shops_name - 店铺ID
	 */
	public String getShopsName() {
		return shopsName;
	}

	/**
	 * 设置店铺ID
	 *
	 * @param shopsName 店铺ID
	 */
	public void setShopsName(String shopsName) {
		this.shopsName = shopsName;
	}

	/**
	 * @return merchant_id
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * 获取	活动ID
	 *
	 * @return inno72_activity_id - 	活动ID
	 */
	public String getInno72ActivityId() {
		return inno72ActivityId;
	}

	/**
	 * 设置	活动ID
	 *
	 * @param inno72ActivityId    活动ID
	 */
	public void setInno72ActivityId(String inno72ActivityId) {
		this.inno72ActivityId = inno72ActivityId;
	}

	/**
	 * 获取活动计划ID
	 *
	 * @return inno72_activity_plan_id - 活动计划ID
	 */
	public String getInno72ActivityPlanId() {
		return inno72ActivityPlanId;
	}

	/**
	 * 设置活动计划ID
	 *
	 * @param inno72ActivityPlanId 活动计划ID
	 */
	public void setInno72ActivityPlanId(String inno72ActivityPlanId) {
		this.inno72ActivityPlanId = inno72ActivityPlanId;
	}

	/**
	 * 获取下单时间
	 *
	 * @return order_time - 下单时间
	 */
	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	/**
	 * 设置下单时间
	 *
	 * @param orderTime 下单时间
	 */
	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	/**
	 * 获取订单金额
	 *
	 * @return order_price - 订单金额
	 */
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	/**
	 * 设置订单金额
	 *
	 * @param orderPrice 订单金额
	 */
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	/**
	 * @return pay_price
	 */
	public BigDecimal getPayPrice() {
		return payPrice;
	}

	/**
	 * @param payPrice
	 */
	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	/**
	 * 获取订单类型 10
	 *
	 * @return order_type - 订单类型 10
	 */
	public Integer getOrderType() {
		return orderType;
	}

	/**
	 * 设置订单类型 10
	 *
	 * @param orderType 订单类型 10
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return pay_status
	 */
	public Integer getPayStatus() {
		return payStatus;
	}

	/**
	 * @param payStatus
	 */
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	/**
	 * @return goods_status
	 */
	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	/**
	 * @param goodsStatus
	 */
	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	/**
	 * @return pay_time
	 */
	public LocalDateTime getPayTime() {
		return payTime;
	}

	/**
	 * @param payTime
	 */
	public void setPayTime(LocalDateTime payTime) {
		this.payTime = payTime;
	}

	/**
	 * @return ref_order_status
	 */
	public String getRefOrderStatus() {
		return refOrderStatus;
	}

	/**
	 * @param refOrderStatus
	 */
	public void setRefOrderStatus(String refOrderStatus) {
		this.refOrderStatus = refOrderStatus;
	}

	/**
	 * 获取三方OrderId
	 *
	 * @return ref_order_id - 三方OrderId
	 */
	public String getRefOrderId() {
		return refOrderId;
	}

	/**
	 * 设置三方OrderId
	 *
	 * @param refOrderId 三方OrderId
	 */
	public void setRefOrderId(String refOrderId) {
		this.refOrderId = refOrderId;
	}

	/**
	 * 获取是否为重复单（0:不是，1:是）
	 *
	 * @return repetition - 是否为重复单（0:不是，1:是）
	 */
	public Integer getRepetition() {
		return repetition;
	}

	/**
	 * 设置是否为重复单（0:不是，1:是）
	 *
	 * @param repetition 是否为重复单（0:不是，1:是）
	 */
	public void setRepetition(Integer repetition) {
		this.repetition = repetition;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
}