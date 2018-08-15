package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.AlarmUser;

public interface AlarmUserMapper extends Mapper<AlarmUser> {
	public int syncUser();
}