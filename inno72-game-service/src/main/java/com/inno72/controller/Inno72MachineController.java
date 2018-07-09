package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Machine;
import com.inno72.service.Inno72MachineService;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
@RestController
@RequestMapping("/machine")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72MachineController {
	@Resource
	private Inno72MachineService inno72MachineService;

	@RequestMapping(value = "/findGame", method = {RequestMethod.POST, RequestMethod.GET})
	public Result findGame(@RequestParam(name = "machineId") String mid, @RequestParam(name = "gameId") String gameId,
			String version, String versionInno72) {
		return inno72MachineService.findGame(mid, gameId, version, versionInno72);
	}

	@RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
	public Result add(Inno72Machine inno72Machine) {
		inno72MachineService.save(inno72Machine);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public Result delete(@RequestParam Integer id) {
		inno72MachineService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
	public Result update(Inno72Machine inno72Machine) {
		inno72MachineService.update(inno72Machine);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
	public Result detail(@RequestParam Integer id) {
		Inno72Machine inno72Machine = inno72MachineService.findById(id);
		return ResultGenerator.genSuccessResult(inno72Machine);
	}

	@RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<Inno72Machine> list = inno72MachineService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}

}
