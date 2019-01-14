package com.inno72.vo;

import java.util.List;
import java.util.Map;

import com.inno72.model.Inno72MerchantTotalCount;

public class Inno72MerchantTotalCountVo extends Inno72MerchantTotalCount {
	private String code;
	private List<Map<String, Object>> machineInfo;

	private String startTime;
	private String endTime;
	private String totalTime;
	private Integer goodsNum;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Map<String, Object>> getMachineInfo() {
		return machineInfo;
	}

	public void setMachineInfo(List<Map<String, Object>> machineInfo) {
		this.machineInfo = machineInfo;
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

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}


}
