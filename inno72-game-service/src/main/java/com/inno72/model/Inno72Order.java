package com.inno72.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	 * inno72_game.id
	 */
	@Column(name = "game_id")
	private String gameId;

	/**
	 * 下单时间
	 */
	@Column(name = "order_time")
	private LocalDateTime orderTime;

	@Column(name = "order_price")
	private BigDecimal orderPrice;

	/**
	 * 订单类型 10
	 */
	@Column(name = "order_type")
	private String orderType;

	@Column(name = "pay_status")
	private String payStatus;

	@Column(name = "pay_time")
	private LocalDateTime payTime;

	@Column(name = "activity_id")
	private String activityId;

	@Column(name = "ref_order_id")
	private String refOrderId;

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
	 * 获取inno72_game.id
	 *
	 * @return game_id - inno72_game.id
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * 设置inno72_game.id
	 *
	 * @param gameId inno72_game.id
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
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
	 * @return order_price
	 */
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	/**
	 * @param orderPrice
	 */
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	/**
	 * 获取订单类型 10
	 *
	 * @return order_type - 订单类型 10
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * 设置订单类型 10
	 *
	 * @param orderType 订单类型 10
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return pay_status
	 */
	public String getPayStatus() {
		return payStatus;
	}

	/**
	 * @param payStatus
	 */
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
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

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getRefOrderId() {
		return refOrderId;
	}

	public void setRefOrderId(String refOrderId) {
		this.refOrderId = refOrderId;
	}


}