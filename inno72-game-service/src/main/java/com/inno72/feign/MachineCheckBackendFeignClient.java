package com.inno72.feign;

import com.inno72.common.Result;
import com.inno72.machine.vo.SupplyRequestVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient(value = "MACHINECHECKAPPBACKEND",url="http://check.app.36solo.com")
@FeignClient(value = "MACHINECHECKAPPBACKEND")
@SuppressWarnings("rawtypes")
public interface MachineCheckBackendFeignClient {

	@RequestMapping(value = "/machine/channel/findLockGoodsPush", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	Result findLockGoodsPush(SupplyRequestVo vo);

}
