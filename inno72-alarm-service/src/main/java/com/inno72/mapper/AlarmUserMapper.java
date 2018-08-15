package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.AlarmUser;

public interface AlarmUserMapper extends Mapper<AlarmUser> {
	public int syncUser();

	List<AlarmUser> queryForPage(AlarmUser alarmUser);

	AlarmUser selectByLoginName(String loginName);
}