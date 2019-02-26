package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72ActivityIndex;

public interface Inno72ActivityIndexMapper extends Mapper<Inno72ActivityIndex> {
	List<Inno72ActivityIndex> selectIndex(Map<String, Object> param);
}