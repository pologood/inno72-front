package com.inno72.service;

import com.inno72.common.Result;

public interface Inno72FileService {

	public Result<Object> skindetect(String sessionUUid, String base64Pic);

}
