package com.inno72.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.service.SuperOpenService;

@RestController
@RequestMapping("/inno72")
public class Inno72OpenConrolelr {
	
	@Autowired
	private SuperOpenService superOpenService;
	
	@RequestMapping(value = "/service/open/{serviceName}/{V}", method = {RequestMethod.GET , RequestMethod.POST})
	public String open(
			HttpServletRequest request, 
			HttpServletResponse response, 
			@PathVariable("serviceName") String serviceName,
			@PathVariable("V") String v) {
		return "forward:/"+superOpenService.adpter(request, response, serviceName, v);
	}

}
