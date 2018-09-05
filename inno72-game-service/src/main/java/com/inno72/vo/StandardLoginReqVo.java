package com.inno72.vo;

public class StandardLoginReqVo {

	private String machineCode;

	private String userName;

	private String passwd;

	private Integer isNeedQrCode;

	private Integer loginType;

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Integer getIsNeedQrCode() {
		return isNeedQrCode;
	}

	public void setIsNeedQrCode(Integer isNeedQrCode) {
		this.isNeedQrCode = isNeedQrCode;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	@Override
	public String toString() {
		return "StandardLoginReqVo [machineCode=" + machineCode + ", userName=" + userName + ", passwd=" + passwd
				+ ", isNeedQrCode=" + isNeedQrCode + ", loginType=" + loginType + "]";
	}


}
