package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.AlarmRule;

public interface AlarmRuleMapper extends Mapper<AlarmRule> {
	List<AlarmRule> queryForPage(AlarmRule alarmRule);
}