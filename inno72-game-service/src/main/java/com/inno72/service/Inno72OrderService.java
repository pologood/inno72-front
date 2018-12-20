package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.model.Inno72Order;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.OrderVo;

import java.util.List;

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
	 */
    void updateOrderStatusAndPayStatus(String outTradeNo, Integer orderStatus, Integer payStatus);

	/**
	 *分页查询订单
	 * @param gameUserId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
    List<OrderVo> orderList(String gameUserId, Integer pageNum, Integer pageSize);
}
