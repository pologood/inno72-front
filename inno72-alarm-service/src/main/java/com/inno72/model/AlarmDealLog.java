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

@Table(name = "alarm_deal_log")
public class AlarmDealLog {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "rule_id")
    private String ruleId;

    private Integer status;

    @Column(name = "first_read_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime firstReadTime;

    @Column(name = "deal_user")
    private String dealUser;

    @Column(name = "deal_method")
    private String dealMethod;

    @Column(name = "deal_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dealTime;

    @Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    @Column(name = "update_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updateTime;

    private AlarmRule alarmRule;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LocalDateTime getFirstReadTime() {
		return firstReadTime;
	}

	public void setFirstReadTime(LocalDateTime firstReadTime) {
		this.firstReadTime = firstReadTime;
	}

	public String getDealUser() {
		return dealUser;
	}

	public void setDealUser(String dealUser) {
		this.dealUser = dealUser;
	}

	public String getDealMethod() {
		return dealMethod;
	}

	public void setDealMethod(String dealMethod) {
		this.dealMethod = dealMethod;
	}

	public LocalDateTime getDealTime() {
		return dealTime;
	}

	public void setDealTime(LocalDateTime dealTime) {
		this.dealTime = dealTime;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public AlarmRule getAlarmRule() {
		return alarmRule;
	}

	public void setAlarmRule(AlarmRule alarmRule) {
		this.alarmRule = alarmRule;
	}
}