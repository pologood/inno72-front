package com.inno72.vo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.model.Inno72MerchantTotalCount;

import lombok.Data;

@Data
public class Inno72MerchantTotalCountVo extends Inno72MerchantTotalCount {
	private String code;
	private List<Map<String, Object>> machineInfo;

	private String startTime;
	private String endTime;
	private String totalTime;
	private Integer goodsNum;

	public String getTotalTime(){
		if (StringUtil.notEmpty(getStartTime()) && StringUtil.isEmpty(totalTime)){
			Duration between = Duration
					.between(LocalDateTimeUtil.transfer(getStartTime()), LocalDateTime.now());
			return between.toHours()+"";
		}
		return "";
	}

	public String getStartTime() {
		if (StringUtil.isEmpty(this.startTime)){
			return "2018-12-01 10:53:50";
		}
		return startTime;
	}

	public String getEndTime() {
		if (StringUtil.isEmpty(this.endTime)){
			return "2018-12-31 10:53:50";
		}
		return endTime;
	}
}
