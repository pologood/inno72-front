package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72GameUserLife;

public interface Inno72GameUserLifeMapper extends Mapper<Inno72GameUserLife> {
	Inno72GameUserLife selectByUserChannelIdLast(String userId);
}