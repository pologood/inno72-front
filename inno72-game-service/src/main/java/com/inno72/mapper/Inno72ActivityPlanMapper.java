package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72ActivityPlan;
import com.inno72.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

public interface Inno72ActivityPlanMapper extends Mapper<Inno72ActivityPlan> {
	List<Inno72ActivityPlan> selectByMachineId(String machineId);

	String selectCouponCodeByParam(Map<String, String> selectCouponParam);

	Inno72ActivityPlan selectDefaultActPlan();

    List<String> findActivityPlanIdByMid(@Param(value="mid") String mid);

    List<GoodsVo> getGoodsList(@Param(value="activityPlanId")String activityPlanId,@Param(value="mid") String mid);
}