package com.inno72.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateSerializer;
import com.inno72.common.CustomLocalDateTimeSerializer;

@Table(name = "inno72_activity_info_desc")
public class Inno72ActivityInfoDesc {
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
	 * 活动信息类型 1：新增；
	 */
	@Column(name = "info_type")
	private Integer infoType;

	@Column(name = "info_date")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@DateTimeFormat(pattern = "yyyy年MM月dd日")
	private LocalDate infoDate;

	@Column(name = "info_desc")
	private String infoDesc;

	@Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	private String creator;

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
	 * @return machine_id - 商户ID inno72_merchant_user.merchant_id
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
	 * 获取活动信息类型 1：新增；
	 *
	 * @return info_type - 活动信息类型 1：新增；
	 */
	public Integer getInfoType() {
		return infoType;
	}

	/**
	 * 设置活动信息类型 1：新增；
	 *
	 * @param infoType 活动信息类型 1：新增；
	 */
	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}

	/**
	 * @return info_date
	 */
	public LocalDate getInfoDate() {
		return infoDate;
	}

	/**
	 * @param infoDate
	 */
	public void setInfoDate(LocalDate infoDate) {
		this.infoDate = infoDate;
	}

	/**
	 * @return info_desc
	 */
	public String getInfoDesc() {
		return infoDesc;
	}

	/**
	 * @param infoDesc
	 */
	public void setInfoDesc(String infoDesc) {
		this.infoDesc = infoDesc;
	}

	/**
	 * @return create_time
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
}