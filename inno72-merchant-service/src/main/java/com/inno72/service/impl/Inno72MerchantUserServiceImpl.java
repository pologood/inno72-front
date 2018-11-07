package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72MerchantUserMapper;
import com.inno72.model.Inno72MerchantUser;
import com.inno72.service.Inno72MerchantUserService;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
@Service
@Transactional
public class Inno72MerchantUserServiceImpl extends AbstractService<Inno72MerchantUser> implements Inno72MerchantUserService {
    @Resource
    private Inno72MerchantUserMapper inno72MerchantUserMapper;

}
