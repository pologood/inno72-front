package com.inno72.msg.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inno72.common.utils.StringUtil;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckUserService;
import com.inno72.msg.service.MainService;
import com.inno72.msg.util.MsgHandler;

import tk.mybatis.mapper.entity.Condition;

@Service
public class MainServiceImpl implements MainService {
	Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);

	@Autowired
	private CheckUserService checkUserService;

	@Override
	public String processMsg(String msg) {
		logger.info("接收到企业号消息：{}", msg);
		MsgHandler handler = new MsgHandler(msg);
		String msgType = handler.getMsgType();
		String user = handler.getFromuser();
		if ("text".equals(msgType)) {
			String content = handler.getContent();
			if (!StringUtil.isEmpty(content) && content.contains("绑定")) {
				String mobile = content.replace("绑定", "").replace("+", "");
				logger.info("企业号巡检人员绑定手机号:{},userId:{}", mobile, user);
				Condition condition = new Condition(Inno72CheckUser.class);
				condition.createCriteria().andEqualTo("phone", mobile);
				List<Inno72CheckUser> checkUser = checkUserService.findByCondition(condition);
				if (checkUser != null && !checkUser.isEmpty()) {
					Inno72CheckUser cu = checkUser.get(0);
					String userId = cu.getWechatUserId();
					if (StringUtil.isEmpty(userId)) {
						cu.setWechatUserId(user);
						checkUserService.update(cu);
						return handler.toXml("绑定成功");
					} else {
						return handler.toXml("该手机号已绑定");
					}
				} else {
					return handler.toXml("手机号不存在");
				}
			}
			return handler.toXml("我好像听不懂你说什么");
		} else if ("event".equals(msgType)) {
			String event = handler.getEvent();
			if ("subscribe".equals(event)) {
				return handler.toXml("欢迎使用微信企业号，如果您是巡检人员请回复绑定+手机号进行绑定。");
			}

		}
		return null;
	}

}
