package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72ActivityPlanGameResult;
import java.util.List;
import java.util.Map;

public interface Inno72ActivityPlanGameResultMapper extends Mapper<Inno72ActivityPlanGameResult> {
	List<String> selectByActivityPlanId(Map<String,String> params);
}