package com.inno72.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inno72.common.DataSourceKey;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.AlarmMsgTypeMapper;
import com.inno72.mapper.AlarmUserMapper;
import com.inno72.model.AlarmMsgType;
import com.inno72.model.AlarmUser;
import com.inno72.service.CommonService;
import com.inno72.vo.RespCommonVo;

@Service
public class CommonServiceImpl implements CommonService {

	@Resource
	private AlarmMsgTypeMapper alarmMsgTypeMapper;

	@Resource
	private AlarmUserMapper alarmUserMapper;

	@Override
	public Result<RespCommonVo> queryInitParam() {

		List<AlarmMsgType> alarmMsgTypes = alarmMsgTypeMapper.selectAll();
		List<AlarmUser> alarmUsers = alarmUserMapper.selectAll();

		RespCommonVo vo = new RespCommonVo();
		vo.setAlarmMsgTypes(alarmMsgTypes);
		vo.setAlarmUser(alarmUsers);

		return Results.success(vo);
	}
}
