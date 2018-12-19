package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72AdminArea;

public interface Inno72AdminAreaMapper extends Mapper<Inno72AdminArea> {
	List<Inno72AdminArea> findCity();

}