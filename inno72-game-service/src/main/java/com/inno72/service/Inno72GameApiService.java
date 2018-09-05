package com.inno72.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.vo.Inno72SamplingGoods;
import com.inno72.vo.MachineApiVo;

public interface Inno72GameApiService {

	Result<Object> findProduct(MachineApiVo vo);

	Result<Object> order(MachineApiVo vo);

	Result<Boolean> orderPolling(MachineApiVo vo);

	Result<Object> luckyDraw(MachineApiVo vo);

	Result<Object> oneKeyOrder(MachineApiVo vo);

	Result<Object> oneKeyOrderNologin(MachineApiVo vo);

	Result<Object> paiYangOrder(MachineApiVo vo);

	Result<String> shipmentReport(MachineApiVo vo);

	Result<String> shipmentReportV2(MachineApiVo vo);

	Result<String> sessionRedirect(String sessionUuid, String mid, String token, String code, String userId,
			String itemId);

	Result<Object> sessionNologin(String machineCode, Integer isNeedQrCode);

	Result<String> malfunctionLog(String machineId, String channelCode);

	Result<String> shipmentFail(String machineId, String channelCode, String describtion);

	Result<String> userDuration(String token, String itemId, String sellerId, String userId, String machineCode,
			String playTime);

	Result<List<Inno72SamplingGoods>> getSampling(String machineCode);

}
