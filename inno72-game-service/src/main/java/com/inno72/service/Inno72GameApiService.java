package com.inno72.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.vo.Inno72SamplingGoods;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.StandardPrepareLoginReqVo;

public interface Inno72GameApiService {


	Result<Object> orderPolling(MachineApiVo vo);

	Result<Object> standardOrder(MachineApiVo vo);

	Result<Object> oneKeyOrderNologin(MachineApiVo vo);

	Result<String> shipmentReport(MachineApiVo vo);

	Result<String> shipmentReportV2(MachineApiVo vo);

	Result<String> sessionRedirect(String sessionUuid, String mid, String token, String code, String userId,
			String itemId);

	Result<Object> prepareLoginNologin(String machineCode);

	Result<Object> prepareLoginQrCode(StandardPrepareLoginReqVo req);

	String redirectLogin(String sessionUuid);

	Result<String> shipmentFail(String machineId, String channelCode, String describtion);

	Result<List<Inno72SamplingGoods>> getSampling(String machineCode);

	Result<String> setHeartbeat(String machineCode, String page, String planCode, String activity, String desc);
}
