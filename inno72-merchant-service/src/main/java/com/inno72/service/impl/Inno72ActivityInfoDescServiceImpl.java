package com.inno72.service.impl;

import com.inno72.mapper.Inno72ActivityInfoDescMapper;
import com.inno72.model.Inno72ActivityInfoDesc;
import com.inno72.service.Inno72ActivityInfoDescService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/11.
 */
@Service
@Transactional
public class Inno72ActivityInfoDescServiceImpl extends AbstractService<Inno72ActivityInfoDesc> implements Inno72ActivityInfoDescService {
    @Resource
    private Inno72ActivityInfoDescMapper inno72ActivityInfoDescMapper;

}
