package com.framelib.utils.http;

import java.util.Map;

public class HttpRequestProxy implements HttpRequestAPI {

	private HttpRequestAPIImpl httpRquestAPI;

	private HttpRequestProxy(HttpRequestAPIImpl httpRquestAPI) {
		this.httpRquestAPI = httpRquestAPI;
	}

	public static HttpRequestProxy newHttpRequestProxy() {
		return new HttpRequestProxy(new HttpRequestAPIImpl());
	}

	public String requestForGet(String url) {

		return httpRquestAPI.requestForGet(url);
	}

	public String requestForPost(String url, Map<String, Object> paramMap) {

		return httpRquestAPI.requestForPost(url, paramMap);
	}

	@Override
	public String requestForPostWithJsonBody(String url, String json) {
		
		return null;
	}

	@Override
	public String requestForGetWithStreamResponse(String url) {
		
		return null;
	}

}
