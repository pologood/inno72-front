package com.inno72.msg.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.utils.StringUtil;
import com.inno72.msg.aes.WXBizMsgCrypt;
import com.inno72.msg.service.MainService;

@RestController
public class MainController {

	Logger logger = LoggerFactory.getLogger(MainController.class);
	@Autowired
	private MainService mainService;

	@RequestMapping(value = "/main", method = { RequestMethod.POST, RequestMethod.GET })
	public String processMsg(HttpServletRequest request) throws Exception {
		String sToken = "XDubpXOqvnXsPa";
		String sCorpID = "wwdf888217e3fcd510";
		String sEncodingAESKey = "fxjCVo9P6XdGPqjbDg9dW1zmkUJGeSN3xQJYw0JQUDK";
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
		String msg_signature = request.getParameter("msg_signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		if (!StringUtil.isEmpty(echostr)) {
			String sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);
			logger.info("验证url结果返回{}", sEchoStr);
			return sEchoStr;
		}
		byte[] encryptRequestBodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
		String sReqData = new String(encryptRequestBodyBytes);
		String sMsg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, sReqData);
		String returnMsg = mainService.processMsg(sMsg);
		logger.info("返回的明文消息为：{}", returnMsg);
		String sEncryptMsg = wxcpt.EncryptMsg(returnMsg, timestamp, nonce);
		return sEncryptMsg;

	}

}
