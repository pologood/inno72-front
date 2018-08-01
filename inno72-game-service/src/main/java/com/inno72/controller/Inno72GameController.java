package com.inno72.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Game;
import com.inno72.service.Inno72GameService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
@RestController
@RequestMapping("/inno72/game")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72GameController {
	@Resource
	private Inno72GameService inno72GameService;

	@RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
	public Result add(Inno72Game inno72Game) {
		inno72GameService.save(inno72Game);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public Result delete(@RequestParam Integer id) {
		inno72GameService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
	public Result update(Inno72Game inno72Game) {
		inno72GameService.update(inno72Game);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
	public Result detail(@RequestParam Integer id) {
		Inno72Game inno72Game = inno72GameService.findById(id);
		return ResultGenerator.genSuccessResult(inno72Game);
	}

	@RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<Inno72Game> list = inno72GameService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}
}
