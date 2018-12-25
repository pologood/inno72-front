package com.inno72.controller;
import com.inno72.service.Inno72OrderRefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Created by CodeGenerator on 2018/12/19.
*/
@RestController
@RequestMapping("/api/refund")
public class Inno72OrderRefundController {

	private static final Logger logger = LoggerFactory.getLogger(Inno72OrderRefundController.class);

    @Resource
    private Inno72OrderRefundService inno72OrderRefundService;

	/**
	 * 退款回调
	 * @param request
	 * @param response
	 * @param retCode 退款状态 0成功
	 * @param fee 退款金额
	 * @param outRefundNo 退款订单id
	 * @param outTradeNo 业务订单id
	 * @param refundId 退款id
	 * @param retMsg 原因
	 * @param spId spId
	 */
	@RequestMapping(value = "/receiveNotify")
	public void receiveNotify(HttpServletRequest request, HttpServletResponse response,
			String retCode, String fee, String outRefundNo, String outTradeNo,
			String refundId, String retMsg, String spId) {

		logger.info("retCode {}, fee {}, outRefundNo {}, outTradeNo {}, refundId {}, retMsg {}, spId {}",
				retCode, fee, outRefundNo, outTradeNo, refundId, retMsg, spId);


//		1.  设置refund_time
//		2.修改订单状态为已经退款
//		3.发模版消息，


	}
}
