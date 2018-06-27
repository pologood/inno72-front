package com.framelib.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpRequestAPIImpl implements HttpRequestAPI {

	CloseableHttpClient httpclient = null;

	public HttpRequestAPIImpl() {

	}

	public void initHttpclient() {
		httpclient = HttpClients.createDefault();
	}

	private HttpGet initHttpGet(String url) {
		HttpGet httpget = new HttpGet(url);
		return httpget;
	}

	public String requestForGet(String url) {
		initHttpclient();
		HttpGet httpget = this.initHttpGet(url);
		CloseableHttpResponse response = null;
		try {

			response = this.httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			httpclientClose();
		}
		return null;
	}

	private void httpclientClose() {
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String requestForPost(String url, Map<String, Object> paramMap) {
		initHttpclient();
		MultipartEntityBuilder meb = MultipartEntityBuilder.create();
		Set<String> set = paramMap.keySet();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object obj = paramMap.get(key);
			if (obj instanceof String) {
				meb.addTextBody(key, obj.toString(), ContentType.APPLICATION_JSON);
			} else if (obj instanceof File) {
				meb.addBinaryBody(key, (File) obj);
			}

		}
		HttpPost httppost = new HttpPost(url);// application/json
		httppost.addHeader("contentType", "application/json");
		httppost.setEntity(meb.build());
		CloseableHttpResponse response = null;
		try {
			response = this.httpclient.execute(httppost);
			String result = EntityUtils.toString(response.getEntity());

			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			httpclientClose();
		}
		return null;
	}

	@Override
	public String requestForPostWithJsonBody(String url, String json) {
		String result = null;
		CloseableHttpClient client = HttpClients.createDefault();

		HttpPost post = new HttpPost(url);
		CloseableHttpResponse reps = null;
		try {
			StringEntity entity1 = new StringEntity(json, "UTF-8");
			post.setEntity(entity1);
			reps = client.execute(post);
			HttpEntity entity = reps.getEntity();
			result = EntityUtils.toString(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String requestForGetWithStreamResponse(String url) {
		return null;
	}

}
