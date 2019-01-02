package com.inno72.service.impl;

import com.inno72.mapper.Inno72InteractMapper;
import com.inno72.model.Inno72Interact;
import com.inno72.service.Inno72InteractService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/02.
 */
@Service
@Transactional
public class Inno72InteractServiceImpl extends AbstractService<Inno72Interact> implements Inno72InteractService {
    @Resource
    private Inno72InteractMapper inno72InteractMapper;

}
