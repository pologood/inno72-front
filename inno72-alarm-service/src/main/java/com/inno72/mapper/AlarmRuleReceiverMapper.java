package com.inno72.mapper;

import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.AlarmRuleReceiver;

public interface AlarmRuleReceiverMapper extends Mapper<AlarmRuleReceiver> {
	int deleteByRuleId(String ruleId);

	void inserts(Map<String,Object> param);
}