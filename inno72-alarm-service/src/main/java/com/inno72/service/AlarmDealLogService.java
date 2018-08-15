package com.inno72.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.AlarmDealLog;
import com.inno72.vo.AlarmDealLogVo;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
public interface AlarmDealLogService extends Service<AlarmDealLog> {

	List<Map<String,String>> queryForPage(AlarmDealLog alarmDealLog);

	Result<AlarmDealLogVo> selectDetailById(String logId);
}
