package com.inno72.service.impl;

import java.util.List;
import java.util.Map;

import com.inno72.mapper.Inno72AdminAreaMapper;
import com.inno72.model.Inno72AdminArea;
import com.inno72.service.Inno72AdminAreaService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/11/13.
 */
@Service
@Transactional
public class Inno72AdminAreaServiceImpl extends AbstractService<Inno72AdminArea> implements Inno72AdminAreaService {
    @Resource
    private Inno72AdminAreaMapper inno72AdminAreaMapper;

	@Override
	public List<Inno72AdminArea> findCity() {
		return inno72AdminAreaMapper.findCity();
	}
}
