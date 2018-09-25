package com.inno72.machine.vo;

import lombok.Data;

/**
 * 机器日志收集服务
 */
@Data
public class PointLog {

	/**
	 * kafka消费切点字段，如果POINT_TYPE有值则切到 PointLog.java 存储
	 */
	public static final String POINT_TYPE = "logType";

	/**
	 * 机器CODE { OtherLog } -> instanceName
	 *
	 */
	private String machineCode;
	/**
	 * 日志类型
	 */
	private String type;
	/**
	 * 埋点时间{ OtherLog} -> time
	 */
	private String pointTime;
	/**
	 * 标记{ OtherLog} -> tag
	 */
	private String tag;
	/**
	 * 详情描述{ OtherLog} -> detail
	 */
	private String detail;

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPointTime() {
		return pointTime;
	}

	public void setPointTime(String pointTime) {
		this.pointTime = pointTime;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
