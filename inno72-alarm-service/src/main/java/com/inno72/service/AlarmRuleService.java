package com.inno72.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.AlarmRule;
import com.inno72.vo.AlarmRuleRequestVo;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
public interface AlarmRuleService extends Service<AlarmRule> {

	Result<String> addOrUpdate(String json);

	List<AlarmRule> queryForPage(AlarmRule alarmRule);

	AlarmRuleRequestVo queryById(String ruleId);

	Result<String> delete(String id);
}
