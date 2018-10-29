package com.inno72.mapper;

import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72GameUserChannel;

public interface Inno72GameUserChannelMapper extends Mapper<Inno72GameUserChannel> {
	Inno72GameUserChannel selectByChannelUserKey(Map<String, String> paramsChannel);

    String selectUserNickByGameUserId(String userId);
}