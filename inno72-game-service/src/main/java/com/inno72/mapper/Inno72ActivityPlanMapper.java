package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72ActivityPlan;

import java.util.List;
import java.util.Map;

public interface Inno72ActivityPlanMapper extends Mapper<Inno72ActivityPlan> {
	List<Inno72ActivityPlan> selectByMachineId(String machineId);

	String selectCouponCodeByParam(Map<String, String> selectCouponParam);
}