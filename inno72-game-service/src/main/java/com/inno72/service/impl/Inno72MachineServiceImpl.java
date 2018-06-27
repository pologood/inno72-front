package com.inno72.service.impl;

import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.model.Inno72Machine;
import com.inno72.service.Inno72MachineService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72MachineServiceImpl extends AbstractService<Inno72Machine> implements Inno72MachineService {
    @Resource
    private Inno72MachineMapper inno72MachineMapper;

}
