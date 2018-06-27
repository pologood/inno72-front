package com.framelib.utils.http;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

@Deprecated
public class HttpClientManager {

	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 200;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 60000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 20;
	/**
	 * 连接超时时间
	 */
	public final static int CONNECT_TIMEOUT = 10000;
	/**
	 * 读取超时时间
	 */
	public final static int READ_TIMEOUT = 10000;

	private static PoolingHttpClientConnectionManager cm;

	private static CloseableHttpClient httpClient;

	static {
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		// Increase max connections for localhost:80 to 50
		HttpHost localhost = new HttpHost("203.100.94.62", 80);
		cm.setMaxPerRoute(new HttpRoute(localhost), 50);

		httpClient = HttpClients.custom().setConnectionManager(cm).build();
	}

	public static CloseableHttpClient getClient() {
		return httpClient;
	}

}