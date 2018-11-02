package com.inno72.controller;

import javax.annotation.Resource;

import com.inno72.vo.Inno72MachineInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.CommonBean;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.service.PointService;
import com.inno72.vo.UserSessionVo;

@RestController
public class PointController {

	@Autowired
	private GameSessionRedisUtil gameSessionRedisUtil;

	@Resource
	private PointService pointService;

	@RequestMapping(value = "/api/point", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> point(String sessionUuid, String type){

		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (sessionKey == null){
			return Results.failure("session 过期！");
		}
		String msg = "";
		switch (type){
			case CommonBean.POINT_TYPE_FANS:
				pointService.innerPoint(sessionUuid, Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.MEMBERSHIP);
				msg = "用户["+sessionKey.getUserNick()+"]入会成功.";
				break;
			case CommonBean.POINT_TYPE_CONCERN:
				msg = "用户["+sessionKey.getUserNick()+"]关注店铺成功.";
				break;
			default:
				return Results.failure("无此类型！");
		}

		CommonBean.logger(
				type,
				sessionKey.getMachineCode(),
				msg,
				sessionKey.getActivityId()
		);

		return Results.success();
	}

	@RequestMapping(value = "/api/point/information", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> information(@RequestBody String request){
		return pointService.information(request);
	}

}
