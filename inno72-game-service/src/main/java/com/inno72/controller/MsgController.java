package com.inno72.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.msg.MsgUtil;

@RestController
public class MsgController {

	private static final Logger logger = LoggerFactory.getLogger(MsgController.class);

	@Value("${spring.datasource.url}")
	private String url;

	@Autowired
	private MsgUtil msgUtil;

	@ResponseBody
	@RequestMapping(value = "/msg/sendDDTextByGroup", method = {RequestMethod.GET, RequestMethod.POST})
	public String sendDDTextByGroup(String code, String machineCode, String localStr, String channelNum,
			String sendBy) {

		String groupId = "chatefabee5f1110f1a80a0ffe4703aa399c";
		Map<String, String> param = new HashMap<>();
		param.put("machineCode", machineCode);
		param.put("localStr", localStr);
		param.put("text", channelNum);
		try {
			msgUtil.sendDDTextByGroup(code, param, groupId, sendBy);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "ok";
	}

	@ResponseBody
	@RequestMapping(value = "/msg/sendSMS", method = {RequestMethod.GET, RequestMethod.POST})
	public String sendSMS(String code, String phone, String machineCode, String localStr, String text, String sendBy) {
		Map<String, String> param = new HashMap<>();
		param.put("machineCode", machineCode);
		param.put("localStr", localStr);
		param.put("text", text);
		try {
			msgUtil.sendSMS(code, param, phone, sendBy);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "ok";
	}

	@ResponseBody
	@RequestMapping(value = "/msg/sendPush", method = {RequestMethod.GET, RequestMethod.POST})
	public String sendPush(String code, String phone, String machineCode, String localStr, String text, String sendBy) {
		Map<String, String> param = new HashMap<>();
		param.put("machineCode", machineCode);
		param.put("localStr", localStr);
		param.put("text", text);
		try {
			msgUtil.sendPush(code, param, phone, sendBy, "【缺货】您负责的机器需要补货", "");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "ok";
	}

}
