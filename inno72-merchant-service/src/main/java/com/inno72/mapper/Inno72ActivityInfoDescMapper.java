package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72ActivityInfoDesc;

public interface Inno72ActivityInfoDescMapper extends Mapper<Inno72ActivityInfoDesc> {
	List<Inno72ActivityInfoDesc> selectActInfoDesc(Map<String, String> param);
}