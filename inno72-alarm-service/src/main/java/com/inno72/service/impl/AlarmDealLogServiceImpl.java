package com.inno72.service.impl;

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
    private AlarmDealLogMapper alarmDealLogMapper;

    @Resource
    private AlarmDetailLogMapper alarmDetailLogMapper;

	@Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
	public List<Map<String, String>> queryForPage(Map<String, String> params) {
		LOGGER.info("查询列表参数 {}", JSON.toJSONString(params));
		return alarmDealLogMapper.queryForPage(params);
	}

	@Override
	@TargetDataSource(dataSourceKey = DataSourceKey.DB_INNO72SAAS)
	public Result<AlarmDealLogVo> selectDetailById(String logId){

		if (StringUtil.isEmpty(logId)){
			return Results.failure("参数缺失");
		}

		AlarmDealLog alarmDealLog = alarmDealLogMapper.queryDetail(logId);

		if (alarmDealLog == null){
			return Results.failure("非法请求");
		}

		AlarmDealLogVo vo = new AlarmDealLogVo();
		vo.setAlarmDealLog(alarmDealLog);

		return Results.success(vo);
	}
}
