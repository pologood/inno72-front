package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72MachineDeviceMapper;
import com.inno72.model.Inno72MachineDevice;
import com.inno72.service.Inno72MachineDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Inno72MachineDeviceServiceImpl extends AbstractService<Inno72MachineDevice> implements Inno72MachineDeviceService {
    @Autowired
    private Inno72MachineDeviceMapper mapper;
    @Override
    public Inno72MachineDevice findByMachineCode(String machineCode) {
        Inno72MachineDevice param = new Inno72MachineDevice();
        param.setMachineCode(machineCode);
        return mapper.selectOne(param);
    }

    @Override
    public Inno72MachineDevice findByMachineCodeAndSellerId(String machineCode, String sellerId) {
        Inno72MachineDevice param = new Inno72MachineDevice();
        param.setMachineCode(machineCode);
        param.setSellerId(sellerId);
        return mapper.selectOne(param);
    }
}
