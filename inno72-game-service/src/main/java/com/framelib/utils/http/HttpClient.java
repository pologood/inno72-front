package com.framelib.utils.http;

import okhttp3.*;
import okhttp3.Request.Builder;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class HttpClient {

	public static byte[] getBytes(String url) {
		return (byte[]) get(url, Response_Type.Byte);
	}

	/**
	 * 将返回结果转为encoding编码
	 * 
	 * @param url
	 * @param encoding
	 * @return
	 */
	public static String get(String url, String encoding) {
		String result = get(url);
		try {
			result = new String(result.getBytes("ISO8859-1"), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static ConnectionPool pool;
	private static OkHttpClient client;

	static {

		pool = new ConnectionPool();

		client = new OkHttpClient.Builder().connectionPool(pool).build();

	}

	/**
	 * 响应数据类型
	 * 
	 * @author gavin
	 *
	 */
	public enum Response_Type {
		String, ByteStream, Byte, CharStream;
	}

	/**
	 * 请求数据类型
	 * 
	 * @author gavin
	 *
	 */
	public enum Request_Type {
		String, File, Json, Map;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param responseType
	 * @see {Response_Type}
	 * @return
	 */
	public static Object get(String url, Response_Type responseType) {
		Request request = new Request.Builder().url(url).build();
		ResponseBody responseBody = async(request);
		return getResponse(responseBody, responseType);

	}

	/**
	 * get请求
	 *
	 * @param url
	 * @param responseType
	 * @see {Response_Type}
	 * @return
	 */
	public static String get(String url) {
		return get(url, Response_Type.String).toString();
	}

	/**
	 * get请求-异步
	 *
	 * @param url
	 * @param responseType
	 * @see {Response_Type}
	 * @return
	 */
	public static void get(String url, Callback callback) {
		Request request = new Request.Builder().url(url).build();
		sync(request, callback);
	}

	/**
	 * 参数在请求body中的post
	 *
	 * @param url
	 * @param mediaType
	 *            MediaType.parse("application/json; charset=utf-8")
	 * @param requestBody
	 * @param reqType
	 * @param type
	 * @param header
	 * @return
	 */
	public static Object post(String url, MediaType mediaType, Object requestBody, Request_Type reqType,
			Response_Type responseType, Map<String, String> header) {

		RequestBody body = getRequest(reqType, requestBody, mediaType);

		Builder builder = new Request.Builder().url(url).post(body);
		if (header != null) {
			header.forEach((k, v) -> {
				builder.addHeader(k, v);
			});
		}
		Request request = builder.build();

		ResponseBody responseBody = async(request);
		return getResponse(responseBody, responseType);

	}

	/**
	 * 参数在请求body中的post
	 *
	 * @param url
	 * @param mediaType
	 *            text
	 * @param requestBody
	 * @return
	 */
	public static String post(String url, String requestBody) {
		return post(url, MediaType.parse("application/json; charset=utf-8"), requestBody, Request_Type.String,
				Response_Type.String, null).toString();
	}

	/**
	 * 参数在请求body中的post-异步请求
	 *
	 * @param url
	 * @param mediaType
	 *            MediaType.parse("application/json; charset=utf-8")
	 * @param requestBody
	 * @param reqType
	 * @param type
	 * @param header
	 * @param callback
	 * @see{okhttp3.Callback}
	 * @return
	 */
	public static void post(String url, MediaType mediaType, Object requestBody, Request_Type reqType,
			Response_Type responseType, Map<String, String> header, Callback callback) {

		RequestBody body = getRequest(reqType, requestBody, mediaType);

		Builder builder = new Request.Builder().url(url).post(body);
		if (header != null) {
			header.forEach((k, v) -> {
				builder.addHeader(k, v);
			});
		}
		Request request = builder.build();

		sync(request, callback);

	}

	/**
	 * 参数在请求body中的post-异步请求
	 *
	 * @param url
	 * @param requestBody
	 * @param callback
	 * @see{okhttp3.Callback}
	 * @return
	 */
	public static void post(String url, Object requestBody, Callback callback) {
		RequestBody body = getRequest(Request_Type.String, requestBody,
				MediaType.parse("application/json; charset=utf-8"));
		Builder builder = new Request.Builder().url(url).post(body);
		Request request = builder.build();
		sync(request, callback);
	}

	/**
	 * form提交
	 *
	 * @param url
	 * @param mediaType
	 *            MediaType.parse("application/json; charset=utf-8")
	 * @param requestForm
	 * @param reqType
	 * @param type
	 * @param header
	 * @return
	 */
	public static Object form(String url, Map<String, String> requestForm, Response_Type responseType,
			Map<String, String> header) {

		okhttp3.FormBody.Builder builder = new FormBody.Builder();
		requestForm.forEach((k, v) -> {
			builder.add(k, v);
		});
		FormBody formBody = builder.build();

		Builder reqBuilder = new Request.Builder().url(url).post(formBody);
		if (header != null) {
			header.forEach((k, v) -> {
				reqBuilder.addHeader(k, v);
			});
		}

		Request request = reqBuilder.build();

		ResponseBody responseBody = async(request);

		return getResponse(responseBody, responseType);

	}

	/**
	 * form提交
	 *
	 * @param url
	 * @param mediaType
	 *            MediaType.parse("application/json; charset=utf-8")
	 * @param requestForm
	 * @param reqType
	 * @param type
	 * @param header
	 * @return
	 */
	public static String form(String url, Map<String, String> requestForm, Map<String, String> header) {

		okhttp3.FormBody.Builder builder = new FormBody.Builder();
		requestForm.forEach((k, v) -> {
			builder.add(k, v);
		});
		FormBody formBody = builder.build();

		Builder reqBuilder = new Request.Builder().url(url).post(formBody);
		if (header != null) {
			header.forEach((k, v) -> {
				reqBuilder.addHeader(k, v);
			});
		}

		Request request = reqBuilder.build();

		ResponseBody responseBody = async(request);

		return getResponse(responseBody, Response_Type.String).toString();

	}

	/**
	 * form提交-异步
	 *
	 * @param url
	 * @param mediaType
	 *            MediaType.parse("application/json; charset=utf-8")
	 * @param requestForm
	 * @param reqType
	 * @param type
	 * @param header
	 * @return
	 */
	public static void form(String url, Map<String, String> requestForm, Map<String, String> header,
			Callback callback) {
		okhttp3.FormBody.Builder builder = new FormBody.Builder();

		requestForm.forEach((k, v) -> {
			builder.add(k, v);
		});
		FormBody formBody = builder.build();

		Builder reqBuilder = new Request.Builder().url(url).post(formBody);
		if (header != null) {
			header.forEach((k, v) -> {
				reqBuilder.addHeader(k, v);
			});
		}

		Request request = reqBuilder.build();

		sync(request, callback);

	}

	/**
	 * 同步请求
	 * 
	 * @param request
	 * @return
	 */
	private static ResponseBody async(Request request) {
		try {
			return client.newCall(request).execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void sync(Request request, Callback callback) {
		client.newCall(request).enqueue(callback);
	}

	private static RequestBody getRequest(Request_Type reqType, Object requestBody, MediaType mediaType) {
		RequestBody body = null;
		switch (reqType.toString()) {
		case "String":
			body = RequestBody.create(mediaType, (String) requestBody);
			break;
		case "Byte":
			body = RequestBody.create(mediaType, (byte[]) requestBody);
			break;
		case "File":
			body = RequestBody.create(mediaType, (File) requestBody);
			break;
		default:
			break;
		}

		return body;
	}

	private static Object getResponse(ResponseBody responseBody, Response_Type responseType) {
		Object result = null;
		switch (responseType.toString()) {
		case "String":
			try {
				result = responseBody.string();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Byte":
			try {
				result = responseBody.bytes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "ByteStream":
			result = responseBody.byteStream();
			break;
		case "CharStream":
			result = responseBody.charStream();
			break;
		default:
			break;
		}
		return result;
	}

}
