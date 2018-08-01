package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72AwardRecord;
import com.inno72.service.Inno72AwardRecordService;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
@RestController
@RequestMapping("/inno72/award/record")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72AwardRecordController {
	@Resource
	private Inno72AwardRecordService inno72AwardRecordService;

	@RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
	public Result add(Inno72AwardRecord inno72AwardRecord) {
		inno72AwardRecordService.save(inno72AwardRecord);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public Result delete(@RequestParam Integer id) {
		inno72AwardRecordService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
	public Result update(Inno72AwardRecord inno72AwardRecord) {
		inno72AwardRecordService.update(inno72AwardRecord);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
	public Result detail(@RequestParam Integer id) {
		Inno72AwardRecord inno72AwardRecord = inno72AwardRecordService.findById(id);
		return ResultGenerator.genSuccessResult(inno72AwardRecord);
	}

	@RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<Inno72AwardRecord> list = inno72AwardRecordService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}
}
