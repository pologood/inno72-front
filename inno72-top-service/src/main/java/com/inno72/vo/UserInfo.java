package com.inno72.vo;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 4554876185788548516L;

	private String sessionUuid;
	private String mid;
	private String token;
	private String code;
	private String userId;

	private String authInfo;

	public String getSessionUuid() {
		return sessionUuid;
	}

	public void setSessionUuid(String sessionUuid) {
		this.sessionUuid = sessionUuid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}
}
