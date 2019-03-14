package com.inno72.service;

import java.io.IOException;

import com.inno72.model.Inno72MachineConnectionMsg;

public interface Inno72ConnectionService {
    /**
     * 切换活动
     * @param machineCode
     * @param activityId
     * @param activityType
     */
    void changeActivity(String machineCode, String activityId, Integer activityType);

    void send(String machineCode, String activityId, Long version, Integer type, String data);

	void sendMsg(Inno72MachineConnectionMsg msg) throws IOException;

    void callBack(String machineCode, String activityId, Integer type, Long version);
}
