package com.inno72.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.model.Inno72Machine;
import com.inno72.vo.Inno72SamplingGoods;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.StandardPrepareLoginReqVo;
import com.taobao.api.ApiException;
import com.inno72.vo.UserSessionVo;

public interface Inno72GameApiService {


	Result<Object> orderPolling(MachineApiVo vo);

	Result<Object> standardOrder(MachineApiVo vo);

	Result<String> shipmentReport(MachineApiVo vo);

	Result<String> shipmentReportV2(MachineApiVo vo);

	Result<Object> prepareLoginNologin(String machineCode);

	Result<Object> prepareLoginQrCode(StandardPrepareLoginReqVo req);

	Result<String> shipmentFail(String machineId, String channelCode, String describtion);

	Result<List<Inno72SamplingGoods>> getSampling(String machineCode);

	Result<String> setHeartbeat(String machineCode, String page, String planCode, String activity, String desc);

    Result<Object> setChannelInfo(UserSessionVo userSessionVo, Map<String, Object> result, List<String> resultGoodsId);

    Result<Object> lottery(UserSessionVo vo, String ua, String umid, String prizeId);

	/**
	 * 入会
	 * @param sessionUuid
	 * @param sellSessionKey
	 * @param taobaoUserId
	 * @param meberJoinCallBackUrl
	 * @return
	 */
    Result<Object> newRetailmemberJoin(String sessionUuid, String sellSessionKey, String taobaoUserId, String meberJoinCallBackUrl);
}
