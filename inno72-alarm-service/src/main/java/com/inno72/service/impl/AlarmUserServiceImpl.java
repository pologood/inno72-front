package com.inno72.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.annotation.TargetDataSource;
import com.inno72.common.AbstractService;
import com.inno72.common.DataSourceKey;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.AlarmUserMapper;
import com.inno72.model.AlarmUser;
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

    @Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
    public Result<String> syncUser(){
    	LOGGER.info("开始同步 inno72.inno72_user 用户到inno72_saas.alarm_user");
		int i = alarmUserMapper.syncUser();
		LOGGER.info("一共同步 {} 条记录!", i);
		return Results.success();
	}

}
