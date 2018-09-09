package com.inno72.vo;

public class StandardPrepareLoginReqVo {

	private String machineCode;

	private Integer loginType;

	/**
	 * 扩展字段，需要传递json格式数据
	 */
	private String ext;

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
}