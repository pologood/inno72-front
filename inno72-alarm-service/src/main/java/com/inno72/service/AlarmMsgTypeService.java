package com.inno72.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.AlarmMsgType;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
public interface AlarmMsgTypeService extends Service<AlarmMsgType> {

	Result<String> saveOrUpdate(AlarmMsgType alarmMsgType);

	List<AlarmMsgType> queryForPage(AlarmMsgType alarmDealLog);

	Result<AlarmMsgType> selectById(String id);

	Result<String> delete(String id);
}
