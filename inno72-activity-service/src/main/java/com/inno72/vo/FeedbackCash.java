package com.inno72.vo;

public class FeedbackCash {

	private Boolean isCompleted;

	private String phone;

	private String sessionUuid;
	
	private UserSessionVo sessionInfo;

	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSessionUuid() {
		return sessionUuid;
	}

	public void setSessionUuid(String sessionUuid) {
		this.sessionUuid = sessionUuid;
	}

	public UserSessionVo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(UserSessionVo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}
	
	


}
