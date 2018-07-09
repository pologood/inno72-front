package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72MachineGameMapper;
import com.inno72.model.Inno72MachineGame;
import com.inno72.service.Inno72MachineGameService;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72MachineGameServiceImpl extends AbstractService<Inno72MachineGame> implements Inno72MachineGameService {
    @Resource
    private Inno72MachineGameMapper inno72MachineGameMapper;

}
