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

	public String getTotalTime(){
		if (StringUtil.notEmpty(this.startTime) ){
			Duration between = Duration
					.between(LocalDateTimeUtil.transfer(startTime), LocalDateTime.now());
			return between.toHours()+"";
		}
		return "";
	}
}
