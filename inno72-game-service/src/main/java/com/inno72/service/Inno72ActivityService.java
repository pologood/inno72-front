package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.vo.FansActVo;

public interface Inno72ActivityService {

	/**
	 * 同步活动到天猫
	 * @param actId
	 * @return
	 */
	public Result<FansActVo> tianMaoSaveAct(String actId);

}
