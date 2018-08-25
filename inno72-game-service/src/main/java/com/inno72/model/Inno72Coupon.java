package com.inno72.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_coupon")
public class Inno72Coupon {
	/**
	 * 奖券ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 奖券名称
	 */
	private String name;

	/**
	 * 奖券编码
	 */
	private String code;
	/**
	 * 奖券编码
	 */
	@Column(name = "shops_id")
	private String shopsId;

	/**
	 * 活动计划ID
	 */
	@Column(name = "activity_plan_id")
	private String activityPlanId;

	/**
	 * 状态：0正常，1删除
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

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
	 * 获取奖券ID
	 *
	 * @return id - 奖券ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置奖券ID
	 *
	 * @param id 奖券ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取奖券名称
	 *
	 * @return name - 奖券名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置奖券名称
	 *
	 * @param name 奖券名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取奖券编码
	 *
	 * @return code - 奖券编码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置奖券编码
	 *
	 * @param code 奖券编码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取活动计划ID
	 *
	 * @return activity_plan_id - 活动计划ID
	 */
	public String getActivityPlanId() {
		return activityPlanId;
	}

	/**
	 * 设置活动计划ID
	 *
	 * @param activityPlanId 活动计划ID
	 */
	public void setActivityPlanId(String activityPlanId) {
		this.activityPlanId = activityPlanId;
	}

	/**
	 * 获取状态：0正常，1删除
	 *
	 * @return is_delete - 状态：0正常，1删除
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置状态：0正常，1删除
	 *
	 * @param isDelete 状态：0正常，1删除
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

	public String getShopsId() {
		return shopsId;
	}

	public void setShopsId(String shopsId) {
		this.shopsId = shopsId;
	}
}