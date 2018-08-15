package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.AlarmDealLog;

public interface AlarmDealLogMapper extends Mapper<AlarmDealLog> {

	List<Map<String,String>> queryForPage(AlarmDealLog alarmDealLog);

	AlarmDealLog queryDetail(String logId);
}