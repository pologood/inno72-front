package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Machine;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
public interface Inno72AuthInfoService extends Service<Inno72Machine> {


	Result<Object> createQrCode(String machineId);

	Result<Object> sessionPolling(String sessionUuid);
}
