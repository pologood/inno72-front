package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72InteractMapper;
import com.inno72.model.Inno72Interact;
import com.inno72.service.Inno72InteractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Inno72InteractServiceImpl extends AbstractService<Inno72Interact> implements Inno72InteractService {

	@Autowired
	private Inno72InteractMapper inno72InteractMapper;

	@Override
	public String findPlanCodeByMid(String mid) {
		return inno72InteractMapper.findPlanCodeByMid(mid);
	}
}
