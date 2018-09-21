package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72InteractMachine;
import com.inno72.model.Inno72InteractMachineTime;

/**
 * 机器时间表
 */
public interface Inno72InteractMachineTimeService  extends Service<Inno72InteractMachineTime> {
    /**
     * 查询该机器配置的商品信息（当前时间在起止时间内的）
     * @param machineCode 机器编码
     * @return
     */
    Inno72InteractMachine findActiveInteractMachine(String machineCode);
}
