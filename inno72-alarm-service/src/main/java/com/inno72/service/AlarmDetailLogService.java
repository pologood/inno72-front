package com.inno72.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.model.AlarmDetailLog;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
public interface AlarmDetailLogService extends Service<AlarmDetailLog> {

	List<AlarmDetailLog> getList(String logId);
}
