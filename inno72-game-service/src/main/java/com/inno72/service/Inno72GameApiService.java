package com.inno72.service;

import java.util.Map;

import com.inno72.common.Result;
import com.inno72.vo.MachineApiVo;

public interface Inno72GameApiService {

	Result<Map<String, String>> findProduct(MachineApiVo vo);

	Result<Object> order(MachineApiVo vo);

	Result<String> orderPolling(MachineApiVo vo);

	Result<Object> luckyDraw(MachineApiVo vo);

	Result<String> shipmentReport(MachineApiVo vo);

	Result<String> sessionRedirect(String json);


}
