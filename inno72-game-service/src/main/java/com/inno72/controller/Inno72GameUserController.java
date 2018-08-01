package com.inno72.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72GameUser;
import com.inno72.service.Inno72GameUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@RequestMapping("/inno72/game/user")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72GameUserController {
	@Resource
	private Inno72GameUserService inno72GameUserService;

	@RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
	public Result add(Inno72GameUser inno72GameUser) {
		inno72GameUserService.save(inno72GameUser);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public Result delete(@RequestParam Integer id) {
		inno72GameUserService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
	public Result update(Inno72GameUser inno72GameUser) {
		inno72GameUserService.update(inno72GameUser);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
	public Result detail(@RequestParam Integer id) {
		Inno72GameUser inno72GameUser = inno72GameUserService.findById(id);
		return ResultGenerator.genSuccessResult(inno72GameUser);
	}

	@RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<Inno72GameUser> list = inno72GameUserService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}
}
