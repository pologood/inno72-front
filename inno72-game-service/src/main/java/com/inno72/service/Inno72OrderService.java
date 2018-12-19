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

	/**
	 * 修改支付方式
	 * @param sessionUuid
	 * @param payType
	 * @return
	 */
	Inno72Order changePayType(String sessionUuid, Integer payType);

	/**
	 * 更新支付结果
	 * @param outTradeNo
	 * @param pay
	 * @param succ
	 */
    void updateOrderStatusAndPayStatus(String outTradeNo, Integer orderStatus, Integer payStatus);
}
