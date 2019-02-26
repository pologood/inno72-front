package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MachineConnectionMsg;
import org.apache.ibatis.annotations.Param;

public interface Inno72MachineConnectionMsgMapper extends Mapper<Inno72MachineConnectionMsg> {
    void updateMsgStatus(@Param("machineCode") String machineCode,@Param("activityId") String activityId,@Param("type") Integer type,@Param("version") Long version);

    void invalidBeforeMsg(@Param("machineCode") String machineCode,@Param("activityId") String activityId,@Param("type") Integer type,@Param("version") Long version);
}