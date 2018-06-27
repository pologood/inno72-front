package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inno72.common.AbstractService;
import com.inno72.mapper.UserInfoMapper;
import com.inno72.model.UserInfo;
import com.inno72.service.UserInfoService;

@Service
public class UserInfoServiceImpl extends AbstractService<UserInfo> implements UserInfoService{
	
	@Resource
	private UserInfoMapper userInfoMapper;
	
}
