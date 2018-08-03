package com.inno72.msg.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.msg.mapper.Inno72CheckUserMapper;
import com.inno72.msg.model.Inno72CheckUser;
import com.inno72.msg.service.CheckUserService;

/**
 * Created by CodeGenerator on 2018/08/01.
 */
@Service
@Transactional
public class CheckUserServiceImpl extends AbstractService<Inno72CheckUser> implements CheckUserService {
	@Resource
	private Inno72CheckUserMapper inno72CheckUserMapper;

}
