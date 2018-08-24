package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.AlarmRuleMsgType;

public interface AlarmRuleMsgTypeMapper extends Mapper<AlarmRuleMsgType> {
	int deleteByRuleId(String ruleId);

	List<String> selectByRuleId(String ruleId);
}