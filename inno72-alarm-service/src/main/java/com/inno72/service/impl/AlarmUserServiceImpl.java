package com.inno72.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.AlarmUserMapper;
import com.inno72.model.AlarmUser;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AlarmUserService;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
@Service
@Transactional
public class AlarmUserServiceImpl extends AbstractService<AlarmUser> implements AlarmUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmUserServiceImpl.class);
	@Resource
	private AlarmUserMapper alarmUserMapper;
	@Resource
	private IRedisUtil redisUtil;

	@Override
	public Result<String> syncUser(){
		LOGGER.info("开始同步 inno72.inno72_user 用户到inno72_saas.alarm_user");
		int i = alarmUserMapper.syncUser();
		redisUtil.incr("alarm:db:version");
		LOGGER.info("一共同步 {} 条记录!", i);
		return Results.success();
	}

	@Override
	public List<AlarmUser> queryForPage(AlarmUser alarmUser) {
		return alarmUserMapper.queryForPage(alarmUser);
	}

	@Override
	public Result<AlarmUser> login(String loginName, String password) {
		if (StringUtil.isEmpty(loginName) || StringUtil.isEmpty(password)){
			return Results.failure("非法请求!");
		}
		AlarmUser alarmUser = alarmUserMapper.selectByLoginName(loginName);
		if ( alarmUser == null ){
			return Results.failure("无此用户!");
		}
		if (!password.equals("36solo")){
			return Results.failure("密码错误!");
		}
		return Results.success(alarmUser);
	}

}
