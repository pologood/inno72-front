package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Interact;
import com.inno72.vo.Inno72InteractVo;

public interface Inno72InteractMapper extends Mapper<Inno72Interact> {
	Inno72InteractVo findPlanCodeByMid(String mid);
}