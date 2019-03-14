package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MachineConnectionMsg;
import org.apache.ibatis.annotations.Param;

public interface Inno72MachineConnectionMsgMapper extends Mapper<Inno72MachineConnectionMsg> {
    void updateMsgStatus(@Param("machineCode") String machineCode,@Param("activityId") String activityId,@Param("type") Integer type,@Param("version") Long version);

    void invalidBeforeMsg(@Param("machineCode") String machineCode,@Param("activityId") String activityId,@Param("type") Integer type,@Param("version") Long version);

	List<Inno72MachineConnectionMsg> findUnManageMsg();

	void updateStatusById(@Param("id") String id, @Param("status") Integer status);

	List<Inno72MachineConnectionMsg> findUnManageMsgByType(@Param("type") Integer type);

	void updateTimesById(@Param("id") String id, @Param("times") Integer times);
}