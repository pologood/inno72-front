package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72ActivityPlan;
import com.inno72.model.Inno72Game;

import java.util.List;

public interface Inno72ActivityPlanMapper extends Mapper<Inno72ActivityPlan> {
	List<Inno72ActivityPlan> selectByMachineId(String machineId);

}