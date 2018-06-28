package com.inno72.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.service.SuperOpenService;

@RestController
@RequestMapping("/inno72")
public class Inno72SuperOpenConrolelr {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72SuperOpenConrolelr.class);
	
	@Autowired
	private SuperOpenService superOpenService;
	
	/**
	 * @param request reques
	 * @param response response
	 * @param serviceName 服务名称
	 * @param v 版本号
	 */
	@RequestMapping(value = "/service/open", method = {RequestMethod.GET , RequestMethod.POST})
	public void open(
			HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestParam(value = "serviceName", defaultValue = "") String serviceName,
			@RequestParam(value = "V", defaultValue = "") String v) {
		
		try {
			request.getRequestDispatcher(superOpenService.adpter(request, response, serviceName, v)).forward(request, response);
		} catch (Exception e) {
			LOGGER.info("公共开放接口异常 ===> {} ", e.getMessage(), e);
		}
	}

}
