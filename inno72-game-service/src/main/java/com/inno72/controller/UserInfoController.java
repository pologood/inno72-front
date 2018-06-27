package com.inno72.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.model.UserInfo;
import com.inno72.service.UserInfoService;

@RestController
@RequestMapping("/userInfo")
@CrossOrigin
public class UserInfoController {

	@Autowired
	private UserInfoService userInfoService;

	@RequestMapping(value = "/findAll", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<List<UserInfo>> findAll(){
		return Results.success(userInfoService.findAll());
	}

	@RequestMapping(value = "/findByKey", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<UserInfo> findByKey(UserInfo userInfo){
		return Results.success(userInfoService.findById(1));
	}
}
