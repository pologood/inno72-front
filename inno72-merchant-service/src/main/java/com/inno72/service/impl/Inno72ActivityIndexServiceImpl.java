package com.inno72.service.impl;

import com.inno72.mapper.Inno72ActivityIndexMapper;
import com.inno72.model.Inno72ActivityIndex;
import com.inno72.service.Inno72ActivityIndexService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/11.
 */
@Service
@Transactional
public class Inno72ActivityIndexServiceImpl extends AbstractService<Inno72ActivityIndex> implements Inno72ActivityIndexService {
    @Resource
    private Inno72ActivityIndexMapper inno72ActivityIndexMapper;

}
