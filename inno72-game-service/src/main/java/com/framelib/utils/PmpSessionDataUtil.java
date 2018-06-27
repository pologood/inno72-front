package com.framelib.utils;

import com.alibaba.fastjson.JSON;
import com.framelib.common.CommonConstants;
import com.framelib.common.GeneralUser;
import com.framelib.common.PmpSessionData;

import java.util.Optional;

public class PmpSessionDataUtil {
	public static PmpSessionData toSessionData(String jsonSessionData) {
		return JSON.parseObject(jsonSessionData, PmpSessionData.class);
	}

	public static GeneralUser getPcLoginUser(){
		PmpSessionData session = CommonConstants.PMP_SESSION_DATA;
		// 判断token
		if (!Optional.ofNullable(session).isPresent()) {
			return null;
		}
		// 从Memcached获取用户
		GeneralUser mUser = session.getUser();
		// 判断获取到的用户对象
		if (!Optional.ofNullable(mUser).isPresent()) {
			return null;
		}
		return mUser;
	}
}
