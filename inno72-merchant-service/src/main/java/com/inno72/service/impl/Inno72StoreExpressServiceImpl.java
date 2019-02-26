package com.inno72.service.impl;

import com.inno72.mapper.Inno72StoreExpressMapper;
import com.inno72.model.Inno72StoreExpress;
import com.inno72.service.Inno72StoreExpressService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/02.
 */
@Service
@Transactional
public class Inno72StoreExpressServiceImpl extends AbstractService<Inno72StoreExpress> implements Inno72StoreExpressService {
    @Resource
    private Inno72StoreExpressMapper inno72StoreExpressMapper;

}
