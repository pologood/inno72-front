package com.inno72.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.inno72.common.Result;
import com.inno72.model.Inno72SupplyChannel;

@FeignClient("machine-backend")
@SuppressWarnings("rawtypes")
public interface MachineBackgroundFeignClient {
	
	@RequestMapping(value = "/merchant/channel/out/subCount", method = RequestMethod.POST)
	Result subCount(Inno72SupplyChannel supplyChannel);

	@RequestMapping(value = "/merchant/channel/out/get", method = RequestMethod.POST)
	Result getSupplyChannel(Inno72SupplyChannel supplyChannel);

}
