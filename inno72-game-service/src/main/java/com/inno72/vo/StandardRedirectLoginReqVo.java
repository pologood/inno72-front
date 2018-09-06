package com.inno72.vo;

public class StandardRedirectLoginReqVo {

	private String machineId;

	private String sessionUuid;

	private String env;

	private String bluetoothAddAes;

	private String machineCode;

	private Integer loginType;

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getSessionUuid() {
		return sessionUuid;
	}

	public void setSessionUuid(String sessionUuid) {
		this.sessionUuid = sessionUuid;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getBluetoothAddAes() {
		return bluetoothAddAes;
	}

	public void setBluetoothAddAes(String bluetoothAddAes) {
		this.bluetoothAddAes = bluetoothAddAes;
	}

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


}
