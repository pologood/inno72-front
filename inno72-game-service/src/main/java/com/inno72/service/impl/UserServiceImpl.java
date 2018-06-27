package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.UserMapper;
import com.inno72.model.User;
import com.inno72.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}
