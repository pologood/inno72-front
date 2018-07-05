package com.inno72.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.vo.MachineApiVo;

public interface Inno72GameApiService {

	Result<Map<String, List<Inno72SupplyChannel>>> findProduct(MachineApiVo vo);

	Result<Object> order(MachineApiVo vo);

	Result<String> orderPolling(MachineApiVo vo);

	Result<Object> luckyDraw(MachineApiVo vo);

	Result<String> shipmentReport(MachineApiVo vo);

	Result<String> sessionRedirect(String sessionUuid, String mid, String token, String code, String userId);


}
