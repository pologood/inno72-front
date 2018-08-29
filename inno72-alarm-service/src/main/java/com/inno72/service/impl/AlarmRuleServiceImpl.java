package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import com.inno72.mapper.*;
import com.inno72.model.AlarmDealLog;
import com.inno72.model.AlarmRuleMsgType;
import com.inno72.model.AlarmRuleReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.JSR303Util;
import com.inno72.common.utils.StringUtil;
import com.inno72.model.AlarmRule;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AlarmRuleService;
import com.inno72.vo.AlarmRuleRequestVo;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
@Service
@Transactional
public class AlarmRuleServiceImpl extends AbstractService<AlarmRule> implements AlarmRuleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmRuleServiceImpl.class);

    @Resource
    private AlarmRuleMapper alarmRuleMapper;
    @Resource
    private AlarmRuleMsgTypeMapper alarmRuleMsgTypeMapper;
    @Resource
    private AlarmRuleReceiverMapper alarmRuleReceiverMapper;
	@Resource
	private AlarmDealLogMapper alarmDealLogMapper;
	@Resource
	private IRedisUtil redisUtil;

	@Override
	public Result<String> addOrUpdate(String json) {

		if (StringUtil.isEmpty(json)){
			return Results.failure("参数非法!");
		}

		AlarmRuleRequestVo alarmRuleRequestVo = JSON.parseObject(json, AlarmRuleRequestVo.class);

		Result<String> valid = JSR303Util.valid(alarmRuleRequestVo);
		if (valid.getCode() == Result.FAILURE){
			return valid;
		}

		AlarmRule alarmRule = alarmRuleRequestVo.getAlarmRule();
		LocalDateTime now = LocalDateTime.now();

		String alarmRuleId = alarmRule.getId();

		if ( StringUtil.isEmpty(alarmRuleId) ){
			alarmRuleMapper.insert(alarmRule);
			alarmRuleId = alarmRule.getId();
		}else{
			alarmRule.setUpdateTime(now);
			alarmRuleMapper.updateByPrimaryKeySelective(alarmRule);
		}

		int i = alarmRuleMsgTypeMapper.deleteByRuleId(alarmRuleId);
		LOGGER.info("删除 {} 条 ruleId => {}关联的 消息类型", i, alarmRuleId);
		List<String> typeIds = alarmRuleRequestVo.getAlarmRule().getTypeId();
		for (String typeId : typeIds) {
			AlarmRuleMsgType alarmRuleMsgType = new AlarmRuleMsgType();
			alarmRuleMsgType.setMsgTypeId(typeId);
			alarmRuleMsgType.setRuleId(alarmRuleId);
			alarmRuleMsgTypeMapper.insert(alarmRuleMsgType);
		}

		List<String> userIds = alarmRuleRequestVo.getAlarmRule().getUserId();
		int j = alarmRuleReceiverMapper.deleteByRuleId(alarmRuleId);
		LOGGER.info("删除 {} 条 ruleId => {}关联的 消息类型", j, alarmRuleId);

		for (String userId : userIds) {
			AlarmRuleReceiver alarmRuleReceiver = new AlarmRuleReceiver();
			alarmRuleReceiver.setRuleId(alarmRuleId);
			alarmRuleReceiver.setUserId(userId);
			alarmRuleReceiverMapper.insert(alarmRuleReceiver);
		}
		redisUtil.incr("alarm:db:version");
		return Results.success();
	}

	@Override
	public List<AlarmRule> queryForPage(AlarmRule alarmRule) {
		return alarmRuleMapper.queryForPage(alarmRule);
	}

	@Override
	public AlarmRuleRequestVo queryById(String ruleId) {
		AlarmRule alarmRule = alarmRuleMapper.selectByPrimaryKey(ruleId);
		List<String> msgTypeIds = alarmRuleMsgTypeMapper.selectByRuleId(ruleId);
		List<String> userIds = alarmRuleReceiverMapper.selectByRuleId(ruleId);

		AlarmRuleRequestVo vo = new AlarmRuleRequestVo();
		alarmRule.setTypeId(msgTypeIds);
		alarmRule.setUserId(userIds);
		vo.setAlarmRule(alarmRule);
		return vo;
	}

	@Override
	public Result<String> delete(String id) {
		AlarmDealLog alarmDealLog = new AlarmDealLog();
		alarmDealLog.setRuleId(id);
		int i = alarmDealLogMapper.selectCount(alarmDealLog);
		if (i > 0) {
			return Results.failure("报警规则已经被使用!");
		}
		alarmRuleMapper.deleteByPrimaryKey(id);
		return Results.success();
	}

}
