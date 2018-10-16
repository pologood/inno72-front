package com.inno72.controller;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.CrmMemberIdentityGetRequest;
import com.taobao.api.request.CrmMemberJoinurlGetRequest;
import com.taobao.api.request.TmallMarketingFaceSkindetectRequest;
import com.taobao.api.request.TopAuthTokenCreateRequest;
import com.taobao.api.response.CrmMemberIdentityGetResponse;
import com.taobao.api.response.CrmMemberJoinurlGetResponse;
import com.taobao.api.response.TmallMarketingFaceSkindetectResponse;
import com.taobao.api.response.TopAuthTokenCreateResponse;
import org.apache.commons.lang.StringEscapeUtils;

public class TestController {

	public static void main(String[] args) throws ApiException {

		final String URL = "https://eco.taobao.com/router/rest";
		final String APPKEY = "24952134";
		final String SECRET = "67ee063609d7a0a11997168d70b370c0";

		String sessionKey = "6102a29189bea6294fc5ef76a70842f709cdd154828dd64217101303";

		TaobaoClient client = new DefaultTaobaoClient(URL, APPKEY, SECRET);


		CrmMemberJoinurlGetRequest req = new CrmMemberJoinurlGetRequest();
		req.setCallbackUrl("http://inno72top.ews.m.jaeapp.com/test");
		req.setExtraInfo("{\"source\":\"paiyangji\",\"deviceId\":\"testId\",\"itemId\":576069787121}");
		CrmMemberJoinurlGetResponse rsp = client.execute(req, sessionKey);
		System.out.println(rsp.getBody());

//		CrmMemberIdentityGetRequest req = new CrmMemberIdentityGetRequest();
//		req.setExtraInfo("{\"source\":\"paiyangji\",\"deviceId\":\"testId\",\"itemId\":565058963761}");
//		req.setMixNick(Escape.unescape("d01UrP%2BdYSsphXW%2BcTqLpwRmP%2FrLfMf5rcnjO9hg9D8BB8%3D"));
//		CrmMemberIdentityGetResponse rsp = client.execute(req, sessionKey);
//		System.out.println(rsp.getBody());

		//		TaobaoClient client = new DefaultTaobaoClient(URL, APPKEY, SECRET);
//		TopAuthTokenCreateRequest req = new TopAuthTokenCreateRequest();
//		req.setCode("dzZ6GSpiKYkP41dD9F73Vqoi1418779");
//		TopAuthTokenCreateResponse rsp;
//		try {
//			rsp = client.execute(req);
//			String result = rsp.getBody();
//			System.out.println(result);
//		} catch (ApiException e) {
//			e.printStackTrace();
//		}
	}
}
