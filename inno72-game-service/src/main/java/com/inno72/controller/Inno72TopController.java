package com.inno72.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.service.Inno72TopService;

@RestController
@RequestMapping("/inno72/top")
public class Inno72TopController {


	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72TopController.class);

	@Resource
	private Inno72TopService inno72TopService;

	/**
	 * 上报关注日志
	 * @return
	 */
	@RequestMapping(value = "/fllowshopLog", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> fllowshopLog(String sessionUuid, String sellerId) {
		LOGGER.info("fllowshopLog sessionUuid is {}, sellerId is {}", sessionUuid, sellerId);
		inno72TopService.fllowshopLog(sessionUuid, sellerId);
		return Results.success();
	}

}
