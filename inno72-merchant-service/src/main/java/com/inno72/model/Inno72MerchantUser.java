package com.inno72.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	 * 商户id
	 */
	@Column(name = "seller_id")
	private String sellerId;

	/**
	 * 登录名
	 */
	@Column(name = "login_name")
	private String loginName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 商户名称
	 */
	@Column(name = "merchant_name")
	private String merchantName;

	/**
	 * 验证手机号
	 */
	private String phone;

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
	 * 获取商户id
	 *
	 * @return seller_id - 商户id
	 */
	public String getSellerId() {
		return sellerId;
	}

	/**
	 * 设置商户id
	 *
	 * @param sellerId 商户id
	 */
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
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
}