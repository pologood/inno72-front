package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.AlarmDealLog;
import com.inno72.model.AlarmMsgType;

public interface AlarmMsgTypeMapper extends Mapper<AlarmMsgType> {
	List<AlarmMsgType> queryForPage(AlarmMsgType alarmDealLog);
}