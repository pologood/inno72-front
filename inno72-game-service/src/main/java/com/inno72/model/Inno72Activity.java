package com.inno72.model;

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
import com.inno72.common.LocalDateTimeConverter;

@Table(name = "inno72_activity")
public class Inno72Activity {
	/**
	 * 活动ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 活动名称
	 */
	private String name;

	/**
	 * 活动code
	 */
	@Column(name = "code")
	private String code;

	public enum ActivityType {
		COMMON(0), PAIYANG(1);

		private Integer type;

		ActivityType(Integer type){
			this.type = type;
		}

		public Integer getType() {
			return type;
		}
	}

	/**
	 * 活动类型（0：互动游戏 1：派样互动）
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private String shopId;

	/**
	 * 商户ID
	 */
	@Column(name = "seller_id")
	private String sellerId;

	/**
	 * 默认游戏ID
	 */
	@Column(name = "game_id")
	private String gameId;

	/**
	 * 负责人
	 */
	@Column(name = "manager_id")
	private String managerId;

	/**
	 * 状态：0正常，1停止
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	/**
	 * 是否是默认活动：0不是，1是
	 */
	@Column(name = "is_default")
	private Integer isDefault;

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
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateConverter.class)
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime updateTime;

	/**
	 * 获取活动ID
	 *
	 * @return id - 活动ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置活动ID
	 *
	 * @param id 活动ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取活动名称
	 *
	 * @return name - 活动名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置活动名称
	 *
	 * @param name 活动名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取店铺ID
	 *
	 * @return shop_id - 店铺ID
	 */
	public String getShopId() {
		return shopId;
	}

	/**
	 * 设置店铺ID
	 *
	 * @param shopId 店铺ID
	 */
	public void setShopId(String shopId) {
		this.shopId = shopId;
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

	/*
	 * @return manager_id - 负责人
	 */
	public String getManagerId() {
		return managerId;
	}

	/**
	 * 设置负责人
	 *
	 * @param managerId 负责人
	 */
	public void setManagerId(String managerId) {
		this.managerId = managerId;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}