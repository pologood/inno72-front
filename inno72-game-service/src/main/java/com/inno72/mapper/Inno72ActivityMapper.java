package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Activity;
import com.inno72.vo.FansActVo;

public interface Inno72ActivityMapper extends Mapper<Inno72Activity> {
	Inno72Activity selectDefaultAct();

	FansActVo selectTianMaoActVo(String actId);

	List<Inno72Activity> selectActiveByType(Integer type);
}