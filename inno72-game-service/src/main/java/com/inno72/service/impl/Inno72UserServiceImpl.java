package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.QrCodeUtil;
import com.inno72.common.util.UuidUtil;
import com.inno72.mapper.Inno72GameMapper;
import com.inno72.mapper.Inno72MachineGameMapper;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.mapper.Inno72UserMapper;
import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72MachineGame;
import com.inno72.model.Inno72User;
import com.inno72.oss.OSSUtil;
import com.inno72.redis.StringUtil;
import com.inno72.service.Inno72MachineService;
import com.inno72.service.Inno72UserService;
import com.inno72.vo.Inno72MachineVo;


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
