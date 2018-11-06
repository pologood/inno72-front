package com.inno72.controller;

import com.inno72.util.Encodes;
import com.inno72.util.FastJsonUtils;
import com.inno72.util.TaobaoSdkUtils;
import com.inno72.vo.PropertiesBean;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${h5_mobile_url}")
	private String h5MobileUrl;

	@PostConstruct
	public void initClient() {
		client = new DefaultTaobaoClient(propertiesBean.getUrl(), propertiesBean.getAppkey(),
				propertiesBean.getSecret());
	}

	public static final String SAME = "1"; // 是同一个人
	public static final String NOT_SAME = "0"; // 不是同一个人

	public static final String PLAYCODE_ZHS = "21";

	/**
	 * 登录回调接口
	 */
	@RequestMapping("/api/activity/{sessionUuid}/{taobaoUserId}/{env}")
	public void activityIndex(HttpServletResponse response,
			@PathVariable("sessionUuid") String sessionUuid,
			String code,
			@PathVariable("taobaoUserId") String taobaoUserId,
			@PathVariable("env") String env
			)
			throws Exception {
		LOGGER.info("activity code is {}, sessionUuid is {}, taobaoUserId is {},", code, sessionUuid, taobaoUserId);
		if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(sessionUuid)) {

			String authInfo = TaobaoSdkUtils.getAuthInfo(client, code);
			LOGGER.debug("activity authInfo is {}", authInfo);

			String tokenResult = FastJsonUtils.getString(authInfo, "token_result");
			LOGGER.info("activity tokenResult is {}", tokenResult);

			String _taobaoUserId = FastJsonUtils.getString(tokenResult, "taobao_user_nick");
			LOGGER.info("activity taobaoUserId is {}", _taobaoUserId);

			String original = Encodes.decodeBase64String(taobaoUserId);

			String isSame = "";
			if (original.equals(_taobaoUserId)) {
				LOGGER.info("是同一个用户");
				isSame = SAME;
			} else {
				// 否则不是同一个用户
				LOGGER.info("不是同一个用户");
				isSame = NOT_SAME;
			}

			String formatUrl = String.format(h5MobileUrl, env, PLAYCODE_ZHS);

			String url = formatUrl + "index2.html?uId="+taobaoUserId+"&mCode="+sessionUuid+"&isLeft="+isSame;
			LOGGER.info("url is {}", url);
			response.sendRedirect(url);
		}
	}

}
