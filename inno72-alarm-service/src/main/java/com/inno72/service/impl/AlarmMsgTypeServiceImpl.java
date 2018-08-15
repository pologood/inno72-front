package com.inno72.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.annotation.TargetDataSource;
import com.inno72.common.AbstractService;
import com.inno72.common.DataSourceKey;
import com.inno72.mapper.AlarmMsgTypeMapper;
import com.inno72.model.AlarmDealLog;
import com.inno72.model.AlarmMsgType;
import com.inno72.service.AlarmMsgTypeService;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
@Service
@Transactional
public class AlarmMsgTypeServiceImpl extends AbstractService<AlarmMsgType> implements AlarmMsgTypeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmMsgTypeServiceImpl.class);
    @Resource
    private AlarmMsgTypeMapper alarmMsgTypeMapper;


    @Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
    public void save(AlarmMsgType alarmMsgType){
		int insert = alarmMsgTypeMapper.insert(alarmMsgType);
		LOGGER.debug("保存 {} 完成 {}", JSON.toJSONString(alarmMsgType), insert);
	}

	@Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
	public List<AlarmMsgType> queryForPage(AlarmMsgType alarmDealLog) {
		return alarmMsgTypeMapper.queryForPage(alarmDealLog);
	}

}
