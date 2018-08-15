package com.inno72.system.service;

import com.inno72.common.Result;
import com.inno72.common.SessionData;

public interface DDService {

	String process(String data, String signature, String timestamp, String nonce);

	Result<String> getToken();

	Result<String> registryCallback(String url);

	Result<String> updateRegistryCallback(String url);

	Result<String> initDData();

	Result<SessionData> login(String code, String state);

	Result<SessionData> testLogin(String phone, String name);

}
