package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.service.Inno72AuthInfoService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
@RestController
@RequestMapping("/session")
public class Inno72AuthInfoController {
	@Resource
	private Inno72AuthInfoService inno72AuthInfoService;

	@RequestMapping(value = "/createQrCode", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Result<Object> createQrCode(@RequestParam String machineId) {

		return inno72AuthInfoService.createQrCode(machineId);
	}

	@RequestMapping(value = "/polling", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Result<Object> sessionPolling(@RequestParam String sessionUuid) {
		return inno72AuthInfoService.sessionPolling(sessionUuid);
	}

}
