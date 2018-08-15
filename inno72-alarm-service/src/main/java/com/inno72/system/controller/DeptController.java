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
import com.inno72.system.model.Inno72Dept;
import com.inno72.system.service.DeptService;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@RequestMapping("/system/dept")
@CrossOrigin
public class DeptController {
	@Resource
	private DeptService deptService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String keyword) {
		Result<List<Inno72Dept>> list = deptService.findDepts(keyword);
		return ResultPages.page(list);
	}
}
