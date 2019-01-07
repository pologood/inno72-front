package com.inno72.controller;
import com.inno72.common.Result;
import com.inno72.common.json.JsonUtil;
import com.inno72.model.Inno72OrderRefund;
import com.inno72.msg.MsgUtil;
import com.inno72.service.Inno72OrderRefundService;
import com.inno72.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* Created by CodeGenerator on 2018/12/19.
*/
@RestController
@RequestMapping("/api/refund")
public class Inno72OrderRefundController {

	private static final Logger logger = LoggerFactory.getLogger(Inno72OrderRefundController.class);

    @Resource
    private Inno72OrderRefundService inno72OrderRefundService;

	@Autowired
	private MsgUtil msgUtil;

	private static final String APPNAME = "inno72-game-service";

	private static final String SMSCODE = "sms_order_refund";

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

		logger.info("receiveNotify retCode {}, fee {}, outRefundNo {}, outTradeNo {}, refundId {}, retMsg {}, spId {}",
				retCode, fee, outRefundNo, outTradeNo, refundId, retMsg, spId);
		BigDecimal hundred = new BigDecimal("100");
		if (StringUtil.isEmpty(fee) || StringUtil.isEmpty(outRefundNo) || StringUtil.isEmpty(outTradeNo)) {
			logger.error("回调参数有空值！");
			return;
		}

		try {
			BigDecimal feeVal = new BigDecimal(fee);

			int retCodeVal = Integer.valueOf(retCode).intValue();
			Inno72OrderRefund inno72OrderRefund = inno72OrderRefundService.findById(outRefundNo);

			Inno72OrderRefund.REFUND_STATUS status = Inno72OrderRefund.REFUND_STATUS.INREFUND;
			if (StringUtil.isNotEmpty(retCode) && Integer.valueOf(retCode) == Result.SUCCESS) {
				if (retCodeVal == Result.SUCCESS) {
					BigDecimal amount = inno72OrderRefund.getAmount();
					if (amount.multiply(hundred).compareTo(feeVal) == 0) {
						status = Inno72OrderRefund.REFUND_STATUS.SUCCESS;
					} else {
						status = Inno72OrderRefund.REFUND_STATUS.FAIL;
					}
				} else if (retCodeVal == Result.FAILURE) {
					status = Inno72OrderRefund.REFUND_STATUS.FAIL;
				}
			}
			inno72OrderRefund.setStatus(status.getKey());
			inno72OrderRefund.setRefundTime(new Date());
			logger.info("receiveNotify inno72OrderRefund {} ", JsonUtil.toJson(inno72OrderRefund));
			inno72OrderRefundService.update(inno72OrderRefund);
			this.sendMsg(inno72OrderRefund, status, retMsg);
			logger.info("receiveNotify outRefundNo {} 退款成功", outRefundNo);
		} catch (Exception e) {
			logger.error(outRefundNo + "退款失败" + e.getMessage(), e);
		}
	}

	/**
	 * 发送消息
	 * @param orderRefund
	 * @param status
	 * @param msg
	 */
	private void sendMsg(Inno72OrderRefund orderRefund, Inno72OrderRefund.REFUND_STATUS status, String msg) {
		// 【点七二互动】尊敬的用户您的退款申请失败，失败原因#message#。
		// 【点七二互动】尊敬的用户退款#message#元已退还至您账户，请注意查收！
		StringBuffer smsContent = new StringBuffer("尊敬的用户");

		if (status == Inno72OrderRefund.REFUND_STATUS.SUCCESS) {
			smsContent.append("退款");
			smsContent.append(orderRefund.getAmount() + "元");
			smsContent.append("已退还至您账户，请注意查收！");
			Map<String, String> params = new HashMap<>();
			params.put("content", smsContent.toString());
			msgUtil.sendSMS(SMSCODE, params, orderRefund.getPhone(), APPNAME);
		}
	}

	public static void main(String[] args) {
		BigDecimal amount = new BigDecimal("0.01");
		BigDecimal hundred = new BigDecimal(100);
		BigDecimal feeVal = new BigDecimal(1);
		if (amount.multiply(hundred).compareTo(feeVal) == 0) {
			System.out.println("1");
		} else {
		}
	}
}
