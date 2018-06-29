package com.inno72.controller;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.persistence.metamodel.SetAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.service.SuperOpenService;

/**
 * inno72 统一开放接口，负责所有客户端交互代理转发。
 * 增加接口时候 增加{@link com.inno72.service.impl.ADPTE_METHOD}
 * 
 * @author zb.zhou
 *
 */
@RestController
@RequestMapping("/inno72")
public class Inno72SuperOpenConrolelr {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72SuperOpenConrolelr.class);
	
	@Autowired
	private SuperOpenService superOpenService;
	
	/**
	 * @param request reques
	 * @param response response
	 * @param requestJson 请求参数
	 * 	ex:
	 *	{
	 *	
	 *	   "serviceName":findGame,
	 *	   "params":{
	 *	        "machineId":"machineId",
	 *	        "gameId":"gameId"
	 *	    },
	 *	    "version":"1.0.0"
	 *	}
	 */
	@RequestMapping(value = "/service/open", method = {RequestMethod.GET , RequestMethod.POST})
	public void open(
			HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestBody String requestJson) {
		
		try {
			request.getRequestDispatcher(superOpenService.adpter(requestJson)).forward(request, response);
		} catch (Exception e) {
			LOGGER.info("公共开放接口异常 ===> {} ", e.getMessage(), e);
		}
	}
	
	/**
	 * 定义转发开放接口错误信息
	 * 
	 * @param request reques
	 * @param response response
	 */
	@RequestMapping(value = "/noMethod/open", method = {RequestMethod.GET , RequestMethod.POST})
	public Result<String> noMethod() {
		return Results.failure("方法不存在!");
	}
	/**
	 *  定义转发开放接口错误信息
	 *  
	 * @param request reques
	 * @param response response
	 */
	@RequestMapping(value = "/noVersion/open", method = {RequestMethod.GET , RequestMethod.POST})
	public Result<String> noVersion() {
		return Results.failure("版本错误!");
	}

}
