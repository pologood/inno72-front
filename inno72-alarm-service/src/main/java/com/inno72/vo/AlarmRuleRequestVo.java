package com.inno72.vo;

import java.util.List;

import com.inno72.model.AlarmRule;

import lombok.Data;

@Data
public class AlarmRuleRequestVo {

	private AlarmRule alarmRule;

	private List<String> userId;

	private List<String> typeId;

}
