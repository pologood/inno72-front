package com.inno72.service.impl;

import java.time.LocalDateTime;
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
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.JSR303Util;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.AlarmMsgTypeMapper;
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
    public Result<String> saveOrUpdate(AlarmMsgType alarmMsgType){
		Result<String> valid = JSR303Util.valid(alarmMsgType);
		if (valid.getCode() == Result.FAILURE){
			LOGGER.info("校验失败 {},  {}", JSON.toJSONString(valid), JSON.toJSONString(alarmMsgType));
			return valid;
		}
		if (StringUtil.isNotEmpty(alarmMsgType.getId())){
			AlarmMsgType old = alarmMsgTypeMapper.selectByPrimaryKey(alarmMsgType.getId());
			if (old == null){
				return Results.failure("非法请求");
			}
			alarmMsgTypeMapper.updateByPrimaryKeySelective(alarmMsgType);
		}else {
			alarmMsgType.setCreateTime(LocalDateTime.now());
			int insert = alarmMsgTypeMapper.insert(alarmMsgType);
		}
		LOGGER.debug("保存 {} 完成 ", JSON.toJSONString(alarmMsgType));
		return Results.success();
	}

	@Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
	public List<AlarmMsgType> queryForPage(AlarmMsgType alarmDealLog) {
		return alarmMsgTypeMapper.queryForPage(alarmDealLog);
	}

	@Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
	public Result<AlarmMsgType> selectById(String id){
		if (StringUtil.isEmpty(id)){
			return Results.failure("非法请求!");
		}
		AlarmMsgType alarmMsgType = alarmMsgTypeMapper.selectByPrimaryKey(id);
		if (alarmMsgType == null){
			return Results.failure("不存在!");
		}
		return Results.success(alarmMsgType);
	}

	@Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
	public Result<String> delete(String id) {
		if (StringUtil.isEmpty(id)){
			return Results.failure("非法请求!");
		}
		AlarmMsgType alarmMsgType = alarmMsgTypeMapper.selectByPrimaryKey(id);
		if (alarmMsgType == null){
			return Results.failure("不存在!");
		}
		int i = alarmMsgTypeMapper.deleteByPrimaryKey(id);
		return Results.success();
	}
}
