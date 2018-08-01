package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72ActivityPlanMachine;

import java.util.List;

public interface Inno72ActivityPlanMachineMapper extends Mapper<Inno72ActivityPlanMachine> {
	List<String> selectByMachineId(String machineId);
}