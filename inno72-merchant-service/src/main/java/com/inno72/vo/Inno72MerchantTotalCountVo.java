package com.inno72.vo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateSerializer;
import com.inno72.common.CustomLocalDateTimeSerializer;
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
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;
	//活动结束时间
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;
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

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
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
