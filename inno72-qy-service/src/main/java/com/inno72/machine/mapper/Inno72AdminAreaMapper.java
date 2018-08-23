package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72AdminArea;

import java.util.List;
import java.util.Map;


@org.apache.ibatis.annotations.Mapper
public interface Inno72AdminAreaMapper extends Mapper<Inno72AdminArea> {
	
	public List<Inno72AdminArea> selectFistLevelArea(Map<String, Object> map);

	public Inno72AdminArea cityLevelArea(Map<String, Object> map);

}