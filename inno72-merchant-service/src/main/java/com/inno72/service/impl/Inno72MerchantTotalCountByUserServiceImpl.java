package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72MerchantTotalCountByUserMapper;
import com.inno72.model.Inno72MerchantTotalCountByUser;
import com.inno72.service.Inno72MerchantTotalCountByUserService;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
@Service
@Transactional
public class Inno72MerchantTotalCountByUserServiceImpl extends AbstractService<Inno72MerchantTotalCountByUser> implements Inno72MerchantTotalCountByUserService {
    @Resource
    private Inno72MerchantTotalCountByUserMapper inno72MerchantTotalCountByUserMapper;

}
