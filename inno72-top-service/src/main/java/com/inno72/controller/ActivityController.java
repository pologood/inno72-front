package com.inno72.controller;

import com.inno72.util.Encodes;
import com.inno72.util.FastJsonUtils;
import com.inno72.util.TaobaoSdkUtils;
import com.inno72.vo.PropertiesBean;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ActivityController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopController.class);

	private TaobaoClient client;

	@Resource
	private PropertiesBean propertiesBean;

	@PostConstruct
	public void initClient() {
		client = new DefaultTaobaoClient(propertiesBean.getUrl(), propertiesBean.getAppkey(),
				propertiesBean.getSecret());
	}
	
	/**
	 * 登录回调接口
	 */
	@RequestMapping("/api/activity/{sessionUuid}/{taobaoUserId}")
	public void activityIndex(HttpServletResponse response,
			@PathVariable("sessionUuid") String sessionUuid,
			String code,
			@PathVariable("taobaoUserId") String taobaoUserId)
			throws Exception {
		LOGGER.info("activity code is {}, sessionUuid is {}, taobaoUserId is {},", code, sessionUuid, taobaoUserId);
		if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(sessionUuid)) {

			String authInfo = TaobaoSdkUtils.getAuthInfo(client, code);
			LOGGER.debug("activity authInfo is {}", authInfo);

			String tokenResult = FastJsonUtils.getString(authInfo, "token_result");
			LOGGER.info("activity tokenResult is {}", tokenResult);

			String _taobaoUserId = FastJsonUtils.getString(tokenResult, "taobao_user_nick");
			LOGGER.info("activity taobaoUserId is {}", _taobaoUserId);

			if (!StringUtils.isEmpty(taobaoUserId)) {
				String original = Encodes.decodeBase64String(taobaoUserId);
				if (original.equals(_taobaoUserId)) {
					// todo 判断是同一个用户
				} else {
					// 否则不是同一个用户
				}
			}

		}
	}

}
