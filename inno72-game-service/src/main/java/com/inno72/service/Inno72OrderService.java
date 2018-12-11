package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.model.Inno72Order;
import com.inno72.vo.MachineApiVo;

public interface Inno72OrderService {
	/**
	 * 统一下单
	 * @param vo
	 * @return
	 */
	public Result<Object> order(MachineApiVo vo);

	/**
	 * 修改订单状态
	 * @param inno72OrderId
	 * @param complete
	 */
    void updateOrderStatus(String inno72OrderId, Integer complete);
}
