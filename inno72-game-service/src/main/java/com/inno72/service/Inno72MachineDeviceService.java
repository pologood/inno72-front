package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72MachineDevice;

public interface Inno72MachineDeviceService extends Service<Inno72MachineDevice> {
    /**
     * 根据机器编号查找
     * @param machineCode
     * @return
     */
    Inno72MachineDevice findByMachineCode(String machineCode);
}
