package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.AlarmDetailLog;

public interface AlarmDetailLogMapper extends Mapper<AlarmDetailLog> {
	List<AlarmDetailLog> queryForPage(String logId);
}