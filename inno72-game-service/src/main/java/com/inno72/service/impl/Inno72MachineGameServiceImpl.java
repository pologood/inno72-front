package com.inno72.service.impl;

import com.inno72.mapper.Inno72MachineGameMapper;
import com.inno72.model.Inno72MachineGame;
import com.inno72.service.Inno72MachineGameService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72MachineGameServiceImpl extends AbstractService<Inno72MachineGame> implements Inno72MachineGameService {
    @Resource
    private Inno72MachineGameMapper inno72MachineGameMapper;

}
