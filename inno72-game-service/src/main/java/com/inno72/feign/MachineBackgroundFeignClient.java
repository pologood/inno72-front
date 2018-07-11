package com.inno72.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.inno72.common.Result;
import com.inno72.model.Inno72SupplyChannel;

//@FeignClient(value = "machine-backend", url = "http://172.16.19.218:8880")
@FeignClient(value = "machineBackend")
@SuppressWarnings("rawtypes")
public interface MachineBackgroundFeignClient {

	@RequestMapping(value = "/machine/channel/out/subCount", method = RequestMethod.POST)
	Result subCount(Inno72SupplyChannel supplyChannel);

	@RequestMapping(value = "/machine/channel/out/get", method = RequestMethod.POST)
	Result getSupplyChannel(Inno72SupplyChannel supplyChannel);

}
