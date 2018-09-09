package com.inno72.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_activity_shops")
public class Inno72ActivityShops {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 活动ID
	 */
	@Column(name = "activity_id")
	private String activityId;

	/**
	 * 店铺ID
	 */
	@Column(name = "shops_id")
	private String shopsId;

	/**
	 * 是否入会
	 */
	@Column(name = "is_vip")
	private Integer isVip;

	/**
	 * 入会key
	 */
	@Column(name = "session_key")
	private String sessionKey;

	/**
	 * 获取ID
	 *
	 * @return id - ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置ID
	 *
	 * @param id
	 *            ID
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
	 * @param activityId
	 *            活动ID
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	/**
	 * 获取店铺ID
	 *
	 * @return shops_id - 店铺ID
	 */
	public String getShopsId() {
		return shopsId;
	}

	public Integer getIsVip() {
		return isVip;
	}

	public void setIsVip(Integer isVip) {
		this.isVip = isVip;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * 设置店铺ID
	 *
	 * @param shopsId
	 *            店铺ID
	 */
	public void setShopsId(String shopsId) {
		this.shopsId = shopsId;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Inno72ActivityPlanGameResult) {
			Inno72ActivityShops result = (Inno72ActivityShops) obj;
			if (this.getActivityId().equals(result.getActivityId()) && this.getShopsId().equals(result.getShopsId())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
