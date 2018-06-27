package com.framelib.utils.http;

import java.util.Map;

public interface HttpRequestAPI {

	/**
	 * HTTP GET 请求
	 * 
	 * @param url
	 * @param parma
	 * @return
	 */
	public String requestForGet(String url);

	/**
	 * HTTP POST 请求
	 * 
	 * @param url
	 * @param parma
	 * @return
	 */
	public String requestForPost(String url, Map<String, Object> paramMap);

	/**
	 * json格式的request的body的post请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	public String requestForPostWithJsonBody(String url, String json);

	/**
	 * 获取数据流
	 * 
	 * @param url
	 * @return
	 */
	public String requestForGetWithStreamResponse(String url);

}
