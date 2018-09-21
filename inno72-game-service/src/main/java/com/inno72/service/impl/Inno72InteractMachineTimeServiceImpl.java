package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72InteractMachineMapper;
import com.inno72.model.Inno72GameUser;
import com.inno72.model.Inno72InteractMachine;
import com.inno72.model.Inno72InteractMachineTime;
import com.inno72.service.Inno72InteractMachineTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class Inno72InteractMachineTimeServiceImpl  extends AbstractService<Inno72InteractMachineTime> implements Inno72InteractMachineTimeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72InteractMachineTimeServiceImpl.class);
    @Autowired
    private Inno72InteractMachineMapper inno72InteractMachineMapper ;
    @Override
    public Inno72InteractMachine findActiveInteractMachine(String machineCode) {
        List<Inno72InteractMachine> list = inno72InteractMachineMapper.findActiveInteractMachine(machineCode);
        if(list!=null&&list.size()>0){
            if(list.size()>1){
                LOGGER.info("findActiveInteractMachine,机器配置时间重复machineCode = {}",machineCode);
            }
            return list.get(0);
        }
        return null;
    }
}
