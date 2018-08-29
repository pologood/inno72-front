package com.inno72.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.AlarmDetailLogMapper;
import com.inno72.model.AlarmDetailLog;
import com.inno72.service.AlarmDetailLogService;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
@Service
@Transactional
public class AlarmDetailLogServiceImpl extends AbstractService<AlarmDetailLog> implements AlarmDetailLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmDetailLogServiceImpl.class);

    @Resource
    private AlarmDetailLogMapper alarmDetailLogMapper;

	@Override
	public List<AlarmDetailLog> queryForPage(String logId) {

		LOGGER.info("查询列表参数 logid => {}", logId);

		return alarmDetailLogMapper.queryForPage(logId);
	}

	@Override
	public AlarmDetailLog findById(String id){
		return alarmDetailLogMapper.selectByPrimaryKey(id);
	}
}
