package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.annotation.TargetDataSource;
import com.inno72.common.AbstractService;
import com.inno72.common.DataSourceKey;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.JSR303Util;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.AlarmMsgTypeMapper;
import com.inno72.mapper.AlarmRuleMapper;
import com.inno72.mapper.AlarmRuleMsgTypeMapper;
import com.inno72.mapper.AlarmRuleReceiverMapper;
import com.inno72.mapper.AlarmUserMapper;
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
	private IRedisUtil redisUtil;


	@Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
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
		List<String> typeId = alarmRuleRequestVo.getTypeId();
		if (typeId != null && typeId.size() > 0){
			Map<String, Object> param = new HashMap<>();
			param.put("ruleId", alarmRuleId);
			param.put("list", typeId);
			alarmRuleMsgTypeMapper.inserts(param);
		}

		List<String> userId = alarmRuleRequestVo.getUserId();
		int j = alarmRuleReceiverMapper.deleteByRuleId(alarmRuleId);
		LOGGER.info("删除 {} 条 ruleId => {}关联的 消息类型", j, alarmRuleId);
		if (userId != null && userId.size() > 0){
			Map<String, Object> param = new HashMap<>();
			param.put("ruleId", alarmRuleId);
			param.put("list", userId);
			alarmRuleReceiverMapper.inserts(param);
		}

		redisUtil.incr("alarm:db:version");

		return Results.success();
	}

	@Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
	public List<AlarmRule> queryForPage(AlarmRule alarmRule) {
		return alarmRuleMapper.queryForPage(alarmRule);
	}

	@Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
	public AlarmRuleRequestVo queryById(String ruleId) {
		AlarmRule alarmRule = alarmRuleMapper.selectByPrimaryKey(ruleId);
		List<String> msgTypeIds = alarmRuleMsgTypeMapper.selectByRuleId(ruleId);
		List<String> userIds = alarmRuleReceiverMapper.selectByRuleId(ruleId);

		AlarmRuleRequestVo vo = new AlarmRuleRequestVo();
		vo.setTypeId(msgTypeIds);
		vo.setUserId(userIds);
		vo.setAlarmRule(alarmRule);
		return vo;
	}
}
