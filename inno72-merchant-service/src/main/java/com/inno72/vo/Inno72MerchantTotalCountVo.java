package com.inno72.vo;

import java.util.List;
import java.util.Map;

import com.inno72.model.Inno72ActivityIndex;
import com.inno72.model.Inno72MerchantTotalCount;

public class Inno72MerchantTotalCountVo extends Inno72MerchantTotalCount {

	//忘了干啥用的
	private String code;

	/**
	 * 每个城市的机器数量 count(distinct im.machine_id) as num, area.city as address
	 */
	private List<Map<String, Object>> machineInfo;

	//活动开始时间
	private String startTime;
	//活动结束时间
	private String endTime;
	//总耗时
	private String totalTime;
	//出货数量
	private Integer goodsNum;

	//指标
	private List<Inno72ActivityIndex> indexList;

	public List<Inno72ActivityIndex> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<Inno72ActivityIndex> indexList) {
		this.indexList = indexList;
	}

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
