package com.inno72.service;

import com.inno72.common.Result;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
public interface Inno72AuthInfoService {

	Result<Object> createQrCode(String machineId);

	Result<Object> sessionPolling(String sessionUuid);
}
