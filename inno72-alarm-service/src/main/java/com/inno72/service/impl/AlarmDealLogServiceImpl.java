package com.inno72.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.inno72.mapper.AlarmRuleMapper;
import com.inno72.mapper.AlarmUserMapper;
import com.inno72.model.AlarmRule;
import com.inno72.model.AlarmUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.AlarmDealLogMapper;
import com.inno72.mapper.AlarmDetailLogMapper;
import com.inno72.model.AlarmDealLog;
import com.inno72.service.AlarmDealLogService;
import com.inno72.vo.AlarmDealLogVo;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
@Service
@Transactional
public class AlarmDealLogServiceImpl extends AbstractService<AlarmDealLog> implements AlarmDealLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmDealLogServiceImpl.class);

	@Resource
	private AlarmUserMapper alarmUserMapper;

	@Resource
	private AlarmRuleMapper alarmRuleMapper;

    @Resource
    private AlarmDealLogMapper alarmDealLogMapper;

    @Resource
    private AlarmDetailLogMapper alarmDetailLogMapper;



	@Override
	public List<Map<String, String>> queryForPage(Map<String, String> params) {
		LOGGER.info("查询列表参数 {}", JSON.toJSONString(params));
		return alarmDealLogMapper.queryForPage(params);
	}

	@Override
	public Result<AlarmDealLogVo> selectDetailById(String logId){

		if (StringUtil.isEmpty(logId)){
			return Results.failure("参数缺失");
		}

		AlarmDealLog alarmDealLog = alarmDealLogMapper.selectByPrimaryKey(logId);
		String ruleId = alarmDealLog.getRuleId();
		AlarmRule alarmRule = alarmRuleMapper.selectByPrimaryKey(ruleId);
		alarmDealLog.setAlarmRule(alarmRule);

		String director = alarmRule.getDirector();
		AlarmUser alarmUser = alarmUserMapper.selectByPrimaryKey(director);
		alarmRule.setDirector(alarmUser.getName());

		if (alarmDealLog == null){
			return Results.failure("非法请求");
		}

		AlarmDealLogVo vo = new AlarmDealLogVo();
		vo.setAlarmDealLog(alarmDealLog);

		return Results.success(vo);
	}

	@Override
	public Result<String> addOrUpdate(String json) {
		AlarmDealLog alarmDealLog = JSON.parseObject(json, AlarmDealLog.class);
		if (StringUtil.isNotEmpty(alarmDealLog.getId())) {
			alarmDealLogMapper.updateByPrimaryKeySelective(alarmDealLog);
		} else {
			alarmDealLogMapper.insert(alarmDealLog);
		}
		return Results.success();
	}
}
