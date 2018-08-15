package com.inno72.system.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultPages;
import com.inno72.system.model.Inno72User;
import com.inno72.system.model.Inno72UserRole;
import com.inno72.system.service.UserService;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@RequestMapping("/system/user")
@CrossOrigin
public class UserController {
	@Resource
	private UserService userService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String keyword) {
		Result<List<Inno72User>> list = userService.findUsers(keyword);
		return ResultPages.page(list);
	}

	@RequestMapping(value = "/auth", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> auth(@RequestParam() String userId, @RequestParam() String roleIds) {
		return userService.auth(userId, roleIds);
	}

	@RequestMapping(value = "/queryUserRoles", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72UserRole>> queryUserRoles(@RequestParam() String userId) {
		return userService.queryUserRoles(userId);
	}
}
