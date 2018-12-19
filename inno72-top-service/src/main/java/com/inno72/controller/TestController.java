package com.inno72.controller;

import com.inno72.util.FastJsonUtils;
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

		String test = "{\"code\":0,\"data\":\"{\\\"traceId\\\":\\\"6145709cb9e449f7bfe1d38d1c559dba\\\",\\\"playCode\\\":\\\"p1\\\",\\\"machineCode\\\":\\\"18199586\\\",\\\"sellerId\\\":\\\"1589666223\\\",\\\"sessionKey\\\":\\\"6102622e6fb5de17af88c7be97eb0b10b488a33bbf793431589666223\\\",\\\"goodsCode\\\":\\\"576283105389\\\",\\\"activityType\\\":1,\\\"qrStatus\\\":\\\"0\\\",\\\"isVip\\\":\\\"1\\\"}\",\"msg\":\"成功\"}";
		String traceId = FastJsonUtils.getString(test, "traceId");
		System.out.println(traceId);
	}
}
