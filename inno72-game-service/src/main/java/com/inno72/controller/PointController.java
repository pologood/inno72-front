package com.inno72.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.CommonBean;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.vo.UserSessionVo;

@RestController
public class PointController {

	@Autowired
	private GameSessionRedisUtil gameSessionRedisUtil;

	@RequestMapping(value = "/api/point", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<String> point(String sessionUuid, String type){

		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUuid);
		if (sessionKey == null){
			return Results.failure("session 过期！");
		}
		String msg = "";
		switch (type){
			case CommonBean.POINT_TYPE_FANS:
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

}
