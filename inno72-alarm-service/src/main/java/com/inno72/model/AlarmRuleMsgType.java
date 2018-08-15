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

@Table(name = "alarm_rule_msg_type")
public class AlarmRuleMsgType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 报警规则ID
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * 发送方式ID
     */
    @Column(name = "msg_type_id")
    private String msgTypeId;

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
     * 获取报警规则ID
     *
     * @return rule_id - 报警规则ID
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * 设置报警规则ID
     *
     * @param ruleId 报警规则ID
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * 获取发送方式ID
     *
     * @return msg_type_id - 发送方式ID
     */
    public String getMsgTypeId() {
        return msgTypeId;
    }

    /**
     * 设置发送方式ID
     *
     * @param msgTypeId 发送方式ID
     */
    public void setMsgTypeId(String msgTypeId) {
        this.msgTypeId = msgTypeId;
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