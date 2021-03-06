package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "inno72_merchant_total_count_by_day")
public class Inno72MerchantTotalCountByDay {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String date;

	private String city;

	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * 商品名称
	 */
	@Column(name = "goods_name")
	private String goodsName;

	/**
	 * 活动Id
	 */
	@Column(name = "activity_id")
	private String activityId;

	/**
	 * 活动Name
	 */
	@Column(name = "activity_name")
	private String activityName;

	/**
	 * 停留用户数
	 */
	@Column(name = "stay_num")
	private Integer stayNum;

	/**
	 * 商户ID主键
	 */
	@Column(name = "merchant_id")
	private String merchantId;

	/**
	 * 订单总数（按商品）
	 */
	@Column(name = "order_qty_total")
	private Integer orderQtyTotal;

	/**
	 * 成功订单数
	 */
	@Column(name = "order_qty_succ")
	private Integer orderQtySucc;

	/**
	 * 派发商品数量
	 */
	@Column(name = "goods_num")
	private Integer goodsNum;

	/**
	 * 优惠券发放数量
	 */
	@Column(name = "coupon_num")
	private Integer couponNum;

	/**
	 * 关注用户数
	 */
	@Column(name = "concern_num")
	private Integer concernNum;

	private Integer pv;

	private Integer uv;

	/**
	 * 商户CODE
	 */
	@Column(name = "seller_id")
	private String sellerId;

	@Column(name = "last_update_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdateTime;

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
	 * @return date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return goods_id
	 */
	public String getGoodsId() {
		return goodsId;
	}

	/**
	 * @param goodsId
	 */
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * 获取商品名称
	 *
	 * @return goods_name - 商品名称
	 */
	public String getGoodsName() {
		return goodsName;
	}

	/**
	 * 设置商品名称
	 *
	 * @param goodsName 商品名称
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/**
	 * 获取商户ID主键
	 *
	 * @return merchant_id - 商户ID主键
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * 设置商户ID主键
	 *
	 * @param merchantId 商户ID主键
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * 获取订单总数（按商品）
	 *
	 * @return order_qty_total - 订单总数（按商品）
	 */
	public Integer getOrderQtyTotal() {
		return orderQtyTotal;
	}

	/**
	 * 设置订单总数（按商品）
	 *
	 * @param orderQtyTotal 订单总数（按商品）
	 */
	public void setOrderQtyTotal(Integer orderQtyTotal) {
		this.orderQtyTotal = orderQtyTotal;
	}

	/**
	 * 获取成功订单数
	 *
	 * @return order_qty_succ - 成功订单数
	 */
	public Integer getOrderQtySucc() {
		return orderQtySucc;
	}

	/**
	 * 设置成功订单数
	 *
	 * @param orderQtySucc 成功订单数
	 */
	public void setOrderQtySucc(Integer orderQtySucc) {
		this.orderQtySucc = orderQtySucc;
	}

	/**
	 * 获取派发商品数量
	 *
	 * @return goods_num - 派发商品数量
	 */
	public Integer getGoodsNum() {
		return goodsNum;
	}

	/**
	 * 设置派发商品数量
	 *
	 * @param goodsNum 派发商品数量
	 */
	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	/**
	 * 获取优惠券发放数量
	 *
	 * @return coupon_num - 优惠券发放数量
	 */
	public Integer getCouponNum() {
		return couponNum;
	}

	/**
	 * 设置优惠券发放数量
	 *
	 * @param couponNum 优惠券发放数量
	 */
	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}

	/**
	 * 获取关注用户数
	 *
	 * @return concern_num - 关注用户数
	 */
	public Integer getConcernNum() {
		return concernNum;
	}

	/**
	 * 设置关注用户数
	 *
	 * @param concernNum 关注用户数
	 */
	public void setConcernNum(Integer concernNum) {
		this.concernNum = concernNum;
	}

	/**
	 * @return pv
	 */
	public Integer getPv() {
		return pv;
	}

	/**
	 * @param pv
	 */
	public void setPv(Integer pv) {
		this.pv = pv;
	}

	/**
	 * @return uv
	 */
	public Integer getUv() {
		return uv;
	}

	/**
	 * @param uv
	 */
	public void setUv(Integer uv) {
		this.uv = uv;
	}

	/**
	 * 获取商户CODE
	 *
	 * @return seller_id - 商户CODE
	 */
	public String getSellerId() {
		return sellerId;
	}

	/**
	 * 设置商户CODE
	 *
	 * @param sellerId 商户CODE
	 */
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public LocalDateTime getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Integer getStayNum() {
		return stayNum;
	}

	public void setStayNum(Integer stayNum) {
		this.stayNum = stayNum;
	}
}