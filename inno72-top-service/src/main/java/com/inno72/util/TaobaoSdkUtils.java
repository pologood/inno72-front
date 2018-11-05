package com.inno72.util;

import com.inno72.controller.TopController;
import com.inno72.validator.Validators;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TopAuthTokenCreateRequest;
import com.taobao.api.response.TopAuthTokenCreateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaobaoSdkUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopController.class);

	/**
	 * 获取Access Token
	 * http://open.taobao.com/api.htm?spm=a219a.7386797.0.0.ZOlSkP&source=search&docId=25388&docType=2
	 */
	public static String getAuthInfo(TaobaoClient client, String code) throws ApiException {
		LOGGER.info("getAuthInfo code is {}", code);
		Validators.checkParamNotNull(code);

		TopAuthTokenCreateRequest req = new TopAuthTokenCreateRequest();
		req.setCode(code);
		TopAuthTokenCreateResponse rsp;
		rsp = client.execute(req);
		String result = rsp.getBody();
		LOGGER.info("getAuthInfo result is {}", result);
		return result;
	}

}
