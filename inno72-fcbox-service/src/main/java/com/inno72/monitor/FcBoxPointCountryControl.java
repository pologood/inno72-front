package com.inno72.monitor;

import java.util.List;
import java.util.Map;

public class FcBoxPointCountryControl {

	private String id;

	/**
	 * 状态 -1 失败 0 未开始 1 完成 2 开始 
	 */
	private int state;

	private String startTime;

	private String endTime;

	List<Map<String, String>> param;

	public FcBoxPointCountryControl(String id, int state, String startTime, String endTime,
			List<Map<String, String>> param) {
		this.id = id;
		this.state = state;
		this.startTime = startTime;
		this.endTime = endTime;
		this.param = param;
	}

	public FcBoxPointCountryControl(String id, List<Map<String, String>> param) {
		this.id = id;
		this.state = 0;
		this.param = param;
	}

	public FcBoxPointCountryControl() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<Map<String, String>> getParam() {
		return param;
	}

	public void setParam(List<Map<String, String>> param) {
		this.param = param;
	}
}
