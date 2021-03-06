package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_merchant_user")
public class Inno72MerchantUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT REPLACE(UUID(),'-','')")
	private String id;

	/**
	 * 商户表主键id
	 */
	@Column(name = "merchant_id")
	private String merchantId;

	/**
	 * 登录名
	 */
	@Column(name = "login_name")
	private String loginName;

	/**
	 * 密码
	 */
	@NotNull
	private String password;

	/**
	 * 商户名称
	 */
	@Column(name = "merchant_name")
	@NotNull(message = "请填写商户名称!")
	@Length(max = 50, message = "商户名称不成超过50个字!")
	private String merchantName;

	/**
	 * 验证手机号
	 */
	private String phone;


	/**
	 * 验证手机号
	 */
	@Column(name = "login_status")
	private String loginStatus;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@Column(name = "creator")
	private String creator;

	/**
	 * 行业
	 */
	@Column(name = "industry")
	private String industry;

	/**
	 * 行业
	 */
	@Column(name = "industry_code")
	private String industryCode;



	/**
	 * 最后更新时间
	 */
	@Column(name = "last_update_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime lastUpdateTime;

	/**
	 * 最后更新人
	 */
	@Column(name = "last_updator")
	private String lastUpdator;

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id 主键
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取商户表主键id
	 *
	 * @return inno72_merchant_id - 商户表主键id
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * 设置商户表主键id
	 *
	 * @param merchantId 商户表主键id
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * 获取登录名
	 *
	 * @return login_name - 登录名
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * 设置登录名
	 *
	 * @param loginName 登录名
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 获取密码
	 *
	 * @return password - 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 *
	 * @param password 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取商户名称
	 *
	 * @return seller_name - 商户名称
	 */
	public String getMerchantName() {
		return merchantName;
	}

	/**
	 * 设置商户名称
	 *
	 * @param merchantName 商户名称
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/**
	 * 获取验证手机号
	 *
	 * @return phone - 验证手机号
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置验证手机号
	 *
	 * @param phone 验证手机号
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public LocalDateTime getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastUpdator() {
		return lastUpdator;
	}

	public void setLastUpdator(String lastUpdator) {
		this.lastUpdator = lastUpdator;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}
}