package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72InteractMachineTime;
import org.apache.ibatis.annotations.Param;

public interface Inno72InteractMachineTimeMapper extends Mapper<Inno72InteractMachineTime> {

    Inno72InteractMachineTime findActiveTimeByInteractMachineId(@Param("interactMachineId") String interactMachineId);
}