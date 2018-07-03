package com.inno72.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72UserMapper;
import com.inno72.model.Inno72User;
import com.inno72.service.Inno72UserService;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72UserServiceImpl extends AbstractService<Inno72User> implements Inno72UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72UserServiceImpl.class);
    @Resource
    private Inno72UserMapper inno72UserMapper;
    

	@Override
	public Inno72User getUser(String username) {
		Inno72User user = inno72UserMapper.selectByUsername(username);
		return user;
	}

	

}
