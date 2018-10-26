package com.inno72.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_shops")
public class Inno72Shops {
	/**
	 * 店铺ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 店铺名称
	 */
	@Column(name = "shop_name")
	private String shopName;

	/**
	 * 店铺编码
	 */
	@Column(name = "shop_code")
	private String shopCode;

	/**
	 * 商户ID
	 */
	@Column(name = "seller_id")
	private String sellerId;

	/**
	 * 状态：0正常，1停止
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	/**
	 *
	 */
	@Column(name = "session_key")
	private String sessionKey;

	/**
	 * 关注sessionKey
	 */
	@Column(name = "focus_session_key")
	private String focusSessionKey;

	/**
	 * 备注描述
	 */
	private String remark;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 获取店铺ID
	 *
	 * @return id - 店铺ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置店铺ID
	 *
	 * @param id 店铺ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取店铺名称
	 *
	 * @return shop_name - 店铺名称
	 */
	public String getShopName() {
		return shopName;
	}

	/**
	 * 设置店铺名称
	 *
	 * @param shopName 店铺名称
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	/**
	 * 获取店铺编码
	 *
	 * @return shop_code - 店铺编码
	 */
	public String getShopCode() {
		return shopCode;
	}

	/**
	 * 设置店铺编码
	 *
	 * @param shopCode 店铺编码
	 */
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	/**
	 * 获取商户ID
	 *
	 * @return seller_id - 商户ID
	 */
	public String getSellerId() {
		return sellerId;
	}

	/**
	 * 设置商户ID
	 *
	 * @param sellerId 商户ID
	 */
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * 获取状态：0正常，1停止
	 *
	 * @return is_delete - 状态：0正常，1停止
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置状态：0正常，1停止
	 *
	 * @param isDelete 状态：0正常，1停止
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 获取备注描述
	 *
	 * @return remark - 备注描述
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注描述
	 *
	 * @param remark 备注描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取创建人
	 *
	 * @return create_id - 创建人
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置创建人
	 *
	 * @param createId 创建人
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取更新人
	 *
	 * @return update_id - 更新人
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置更新人
	 *
	 * @param updateId 更新人
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime 更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getFocusSessionKey() {
		return focusSessionKey;
	}

	public void setFocusSessionKey(String focusSessionKey) {
		this.focusSessionKey = focusSessionKey;
	}
}