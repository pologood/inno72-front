package com.inno72.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.AlarmUser;


/**
 * Created by CodeGenerator on 2018/08/13.
 */
public interface AlarmUserService extends Service<AlarmUser> {

	Result<String> syncUser();

	List<AlarmUser> queryForPage(AlarmUser alarmUser);

	Result<AlarmUser> login(String loginName, String password);

}
