package com.inno72.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72ActivityMapper;
import com.inno72.mapper.Inno72InteractMapper;
import com.inno72.model.Inno72Interact;
import com.inno72.model.Inno72Machine;
import com.inno72.service.Inno72ChannelService;
import com.inno72.service.Inno72MachineService;
import com.inno72.vo.StandardPrepareLoginReqVo;

@Service("XIECHENG")
@Transactional
public class Inno72XieChengChannelServiceImpl implements Inno72ChannelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72XieChengChannelServiceImpl.class);

    @Autowired
    private Inno72GameServiceProperties inno72GameServiceProperties;

	@Autowired
	private Inno72MachineService inno72MachineService;

	@Autowired
	private Inno72InteractMapper inno72InteractMapper;

    @Override
    public String buildQrContent(Inno72Machine inno72Machine, String sessionUuid, StandardPrepareLoginReqVo req){
		//String phoneLoginUrl = "http://game.36solo.com/activity/24/mobile/auth.html?sessionUuid=19782745&activityId=755fb275e95b49dca0dabf24d5ecfa61";
		String phoneLoginUrl = inno72GameServiceProperties.get("phoneLoginUrl");

		if (StringUtil.notEmpty(phoneLoginUrl)){

			String activityId = inno72MachineService.findActivityIdByMachineCode(inno72Machine.getMachineCode());
			Inno72Interact inno72Interact = inno72InteractMapper.selectByPrimaryKey(activityId);
			LOGGER.info("activityId - {}, phoneLoginUrl - {}, req - {}, inno72Interact - {}", activityId, phoneLoginUrl, JSON.toJSONString(req), JSON.toJSONString(inno72Interact));

			return wrapWechatUrl(String.format(phoneLoginUrl, inno72Interact.getPlanCode(), inno72Machine.getMachineCode()) + "&activityId="+activityId);
		}

        return phoneLoginUrl;
    }

	private static String wrapWechatUrl(String redirectUrl){
		String url = null;
		try {
			url = URLEncoder.encode(redirectUrl,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String retUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd2d020e170a05549&redirect_uri="+url+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		LOGGER.info("wrapWechatUrl ={}",retUrl);
		return retUrl;
	}

}
