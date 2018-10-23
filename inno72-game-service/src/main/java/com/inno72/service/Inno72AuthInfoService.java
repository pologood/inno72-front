package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.vo.UserSessionVo;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
public interface Inno72AuthInfoService {

	Result<Object> sessionPolling(String sessionUuid);

	/**
	 * 登录前处理
	 * @return
	 */
	Result<Object> processBeforeLogged(String sessionUuid, String authInfo, String traceId);

	/**
	 * 设置用户已经登录
	 */
	boolean setLogged(String sessionUuid);
}
