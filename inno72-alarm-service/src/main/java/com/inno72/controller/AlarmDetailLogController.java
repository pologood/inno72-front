package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.model.AlarmDetailLog;
import com.inno72.service.AlarmDetailLogService;

/**
 * Created by CodeGenerator on 2018/08/13.
 */
@RestController
@RequestMapping("/alarm/detail/log")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AlarmDetailLogController {
	@Resource
	private AlarmDetailLogService alarmDetailLogService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result add(AlarmDetailLog alarmDetailLog) {
		alarmDetailLogService.save(alarmDetailLog);
		return ResultGenerator.genSuccessResult();
	}
	@RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result delete(@RequestParam String id) {
		alarmDetailLogService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result update(AlarmDetailLog alarmDetailLog) {
		alarmDetailLogService.update(alarmDetailLog);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result detail(@RequestParam String id) {
		AlarmDetailLog alarmDetailLog = alarmDetailLogService.findById(id);
		return ResultGenerator.genSuccessResult(alarmDetailLog);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<AlarmDetailLog> list = alarmDetailLogService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
	public ModelAndView getList(String logId) {
		List<AlarmDetailLog> list = alarmDetailLogService.queryForPage(logId);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
