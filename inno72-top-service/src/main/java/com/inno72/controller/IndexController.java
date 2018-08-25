package com.inno72.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopController.class);

	/**
	 * 回调入口
	 * @return
	 */
	@RequestMapping("/")
	public String index(String top_session) {
		LOGGER.info("top_session is {}", top_session);
		return "点72互动应用";
	}
}
