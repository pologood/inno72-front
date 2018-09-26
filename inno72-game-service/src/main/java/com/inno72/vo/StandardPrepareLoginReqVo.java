package com.inno72.vo;

public class StandardPrepareLoginReqVo {

	private String machineCode;

	private Integer loginType;

	/**
	 * 操作类型 1 生成二维码 2 开始新会话
	 */
	private Integer operType;

	/**
	 * 扩展字段，需要传递json格式数据
	 */
	private String ext;

	/**
	 * 操作类型枚举
	 */
	public enum OperTypeEnum {

		CREATE_QRCODE(1, "生成二维码"), START_SESSION(2, "开始会话");

		private Integer key;
		private String desc;

		OperTypeEnum(Integer key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
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

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}
}