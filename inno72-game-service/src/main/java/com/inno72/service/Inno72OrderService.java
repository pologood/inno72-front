package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.vo.MachineApiVo;

public interface Inno72OrderService {
	/**
	 * 统一下单
	 * @param vo
	 * @return
	 */
	public Result<Object> order(MachineApiVo vo);
}
