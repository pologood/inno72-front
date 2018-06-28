package com.inno72.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.service.SuperOpenService;

@Service
public class SuperOpenServiceImpl implements SuperOpenService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SuperOpenServiceImpl.class);

	@Override
	public String adpter(HttpServletRequest request, HttpServletResponse response, String serviceName, String v) {
		LOGGER.info("inno72 开放接口 ==> serviceName -> {}, version -> {}",serviceName, v);
		Map<String, String[]> parameterMap = request.getParameterMap();
		LOGGER.info("request 参数   ==> parameterMap -> {}", JSON.toJSONString(parameterMap));
		return "/machine/detail";
	}

}
