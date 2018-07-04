package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72GameUser;

public interface Inno72GameUserMapper extends Mapper<Inno72GameUser> {

	Inno72GameUser selectByChannelUserKey(String userId);
}