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
import com.inno72.common.Results;
import com.inno72.system.model.Inno72Role;
import com.inno72.system.service.RoleService;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@RestController
@RequestMapping("/system/role")
@CrossOrigin
public class RoleController {
	@Resource
	private RoleService roleService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestParam String name, @RequestParam(required = false) String auths) {
		return roleService.add(name, auths);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		return roleService.delete(id);
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(@RequestParam String id, @RequestParam String name,
			@RequestParam(required = false) String auths) {
		return roleService.update(id, name, auths);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String keyword) {
		Result<List<Inno72Role>> list = roleService.findRoles(keyword);
		return ResultPages.page(list);
	}

	@RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Role>> all() {
		return Results.success(roleService.findAll());
	}

}
