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

@Table(name = "inno72_game_user_channel")
public class Inno72GameUserChannel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
	private String id;

	/**
	 * 用户昵称
	 */
	@Column(name = "user_nick")
	private String userNick;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 渠道id
	 */
	@Column(name = "channel_id")
	private String channelId;
	/**
	 * 渠道id
	 */
	@Column(name = "game_user_id")
	private String gameUserId;

	/**
	 * 渠道名称
	 */
	@Column(name = "channel_name")
	private String channelName;

	/**
	 * 渠道的用户主键
	 */
	@Column(name = "channel_user_key")
	private String channelUserKey;

	@Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateConverter.class)
	private LocalDateTime createTime;

	/**
	 * accessToken
	 */
	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "seller_id")
	private String sellerId;

	private String ext;
	@Column(name = "login_type")
	private Integer loginType;

	public Inno72GameUserChannel() {
	}

	public Inno72GameUserChannel(String userNick, String phone, String channelId, String gameUserId, String channelName,
			String channelUserKey, String accessToken,Integer loginType) {
		this.userNick = userNick;
		this.phone = phone;
		this.channelId = channelId;
		this.gameUserId = gameUserId;
		this.channelName = channelName;
		this.channelUserKey = channelUserKey;
		this.createTime = LocalDateTime.now();
		this.accessToken = accessToken;
		this.loginType = loginType;
	}

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
	 * 获取用户昵称
	 *
	 * @return user_nick - 用户昵称
	 */
	public String getUserNick() {
		return userNick;
	}

	/**
	 * 设置用户昵称
	 *
	 * @param userNick 用户昵称
	 */
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	/**
	 * 获取手机号码
	 *
	 * @return phone - 手机号码
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置手机号码
	 *
	 * @param phone 手机号码
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取渠道id
	 *
	 * @return channel_id - 渠道id
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * 设置渠道id
	 *
	 * @param channelId 渠道id
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * 获取渠道名称
	 *
	 * @return channel_name - 渠道名称
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * 设置渠道名称
	 *
	 * @param channelName 渠道名称
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * 获取渠道的用户主键
	 *
	 * @return channel_user_key - 渠道的用户主键
	 */
	public String getChannelUserKey() {
		return channelUserKey;
	}

	/**
	 * 设置渠道的用户主键
	 *
	 * @param channelUserKey 渠道的用户主键
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

	public String getGameUserId() {
		return gameUserId;
	}

	public void setGameUserId(String gameUserId) {
		this.gameUserId = gameUserId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
}