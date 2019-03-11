package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Interact;

public interface Inno72InteractMapper extends Mapper<Inno72Interact> {
	String findPlanCodeByMid(String mid);
}