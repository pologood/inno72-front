package com.inno72.service.impl;

import com.inno72.mapper.Inno72GameUserMapper;
import com.inno72.model.Inno72GameUser;
import com.inno72.service.Inno72GameUserService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/03.
 */
@Service
@Transactional
public class Inno72GameUserServiceImpl extends AbstractService<Inno72GameUser> implements Inno72GameUserService {
    @Resource
    private Inno72GameUserMapper inno72GameUserMapper;

}
