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

@Table(name = "inno72_game_user")
public class Inno72GameUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select uuid()")
    private String id;

    @Column(name = "user_nick")
    private String userNick;

    private String phone;

    /**
     * 来源（1000:淘宝....）
     */
    private String channel;

    @Column(name = "channel_user_key")
    private String channelUserKey;

    @Column(name = "create_time")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateConverter.class)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateConverter.class)
    private LocalDateTime updateTime;

    private String operator;

    @Column(name = "operator_id")
    private String operatorId;

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
     * @return user_nick
     */
    public String getUserNick() {
        return userNick;
    }

    /**
     * @param userNick
     */
    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取来源（1000:淘宝....）
     *
     * @return channel - 来源（1000:淘宝....）
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 设置来源（1000:淘宝....）
     *
     * @param channel 来源（1000:淘宝....）
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * @return channel_user_key
     */
    public String getChannelUserKey() {
        return channelUserKey;
    }

    /**
     * @param channelUserKey
     */
    public void setChannelUserKey(String channelUserKey) {
        this.channelUserKey = channelUserKey;
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
     * @return update_time
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * @return operator_id
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}