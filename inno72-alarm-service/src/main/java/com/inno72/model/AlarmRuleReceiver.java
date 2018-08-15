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
import com.inno72.common.LocalDateTimeConverter;

@Table(name = "alarm_rule_receiver")
public class AlarmRuleReceiver {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 规则ID
	 */
	@Column(name = "rule_id")
	private String ruleId;

	/**
	 * 接受者ID
	 */
	@Column(name = "user_id")
	private String userId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime createTime;

	@Column(name = "create_user")
	private String createUser;

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
	 * 获取规则ID
	 *
	 * @return rule_id - 规则ID
	 */
	public String getRuleId() {
		return ruleId;
	}

	/**
	 * 设置规则ID
	 *
	 * @param ruleId 规则ID
	 */
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * 获取接受者ID
	 *
	 * @return user_id - 接受者ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置接受者ID
	 *
	 * @param userId 接受者ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * @return create_user
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
}