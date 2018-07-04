package com.inno72.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72AuthenticationMapper;
import com.inno72.model.Inno72Authentication;
import com.inno72.model.Inno72User;
import com.inno72.service.Inno72AuthenticationService;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72AuthenticationServiceImpl extends AbstractService<Inno72Authentication> implements Inno72AuthenticationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72AuthenticationServiceImpl.class);
    @Resource
    private Inno72AuthenticationMapper inno72AuthenticationMapper;
    

	@Override
	public Inno72Authentication getUser(String username) {
		LOGGER.info("根据用户名获取用户", username);
		Inno72Authentication user = inno72AuthenticationMapper.selectByUsername(username);
		LOGGER.info("获取完成 - result -> {}", JSON.toJSONString(user));
		return user;
	}

	

}
