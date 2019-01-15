package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;

@Table(name = "inno72_activity_index")
public class Inno72ActivityIndex {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 活动ID
	 */
	@Column(name = "activity_id")
	private String activityId;

	/**
	 * 活动名称
	 */
	@Column(name = "activity_name")
	private String activityName;

	/**
	 * 商户ID inno72_merchant_user.merchant_id
	 */
	@Column(name = "merchant_id")
	private String merchantId;

	/**
	 * 核心指标类型 1: 参与人数; 2: 商品订单数；3: 商品掉货数
	 */
	@Column(name = "activity_index_type")
	private String activityIndexType;

	/**
	 * 核心指标数量
	 */
	@Column(name = "activity_index")
	private Integer activityIndex;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	private String operator;

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
	 * 获取活动ID
	 *
	 * @return activity_id - 活动ID
	 */
	public String getActivityId() {
		return activityId;
	}

	/**
	 * 设置活动ID
	 *
	 * @param activityId 活动ID
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	/**
	 * 获取活动名称
	 *
	 * @return activity_name - 活动名称
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * 设置活动名称
	 *
	 * @param activityName 活动名称
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * 获取商户ID inno72_merchant_user.merchant_id
	 *
	 * @return merchant_id - 商户ID inno72_merchant_user.merchant_id
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * 设置商户ID inno72_merchant_user.merchant_id
	 *
	 * @param merchantId 商户ID inno72_merchant_user.merchant_id
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * 获取核心指标类型 1: 参与人数; 2: 商品订单数；3: 商品掉货数
	 *
	 * @return activity_index_type - 核心指标类型 1: 参与人数; 2: 商品订单数；3: 商品掉货数
	 */
	public String getActivityIndexType() {
		return activityIndexType;
	}

	/**
	 * 设置核心指标类型 1: 参与人数; 2: 商品订单数；3: 商品掉货数
	 *
	 * @param activityIndexType 核心指标类型 1: 参与人数; 2: 商品订单数；3: 商品掉货数
	 */
	public void setActivityIndexType(String activityIndexType) {
		this.activityIndexType = activityIndexType;
	}

	/**
	 * 获取核心指标数量
	 *
	 * @return activity_index - 核心指标数量
	 */
	public Integer getActivityIndex() {
		return activityIndex;
	}

	/**
	 * 设置核心指标数量
	 *
	 * @param activityIndex 核心指标数量
	 */
	public void setActivityIndex(Integer activityIndex) {
		this.activityIndex = activityIndex;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime 创建时间
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取创建人
	 *
	 * @return creator - 创建人
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * 设置创建人
	 *
	 * @param creator 创建人
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime 更新时间
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
}