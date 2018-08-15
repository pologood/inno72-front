package com.inno72.service;

import java.util.List;

import com.inno72.annotation.TargetDataSource;
import com.inno72.common.DataSourceKey;
import com.inno72.common.Service;
import com.inno72.model.AlarmDealLog;
import com.inno72.model.AlarmMsgType;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
public interface AlarmMsgTypeService extends Service<AlarmMsgType> {

	void save(AlarmMsgType alarmMsgType);

	List<AlarmMsgType> queryForPage(AlarmMsgType alarmDealLog);
}
