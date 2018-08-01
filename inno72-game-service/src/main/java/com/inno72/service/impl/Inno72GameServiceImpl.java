package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72GameMapper;
import com.inno72.model.Inno72Game;
import com.inno72.service.Inno72GameService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72GameServiceImpl extends AbstractService<Inno72Game> implements Inno72GameService {
	@Resource
	private Inno72GameMapper inno72GameMapper;

}
