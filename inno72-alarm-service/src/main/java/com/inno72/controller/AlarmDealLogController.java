package com.inno72.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.model.AlarmDealLog;
import com.inno72.service.AlarmDealLogService;

/**
 * Created by CodeGenerator on 2018/08/13.
 */
@RestController
@RequestMapping("/alarm/deal/log")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AlarmDealLogController {
	@Resource
	private AlarmDealLogService alarmDealLogService;

	@RequestMapping(value = "/save", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String> save(@RequestBody String json){
		return alarmDealLogService.addOrUpdate(json);
	}

	@RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result add(AlarmDealLog alarmDealLog) {
		alarmDealLogService.save(alarmDealLog);
		return ResultGenerator.genSuccessResult();
	}
	@RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result delete(@RequestParam String id) {
		alarmDealLogService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result update(AlarmDealLog alarmDealLog) {
		alarmDealLogService.update(alarmDealLog);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result detail(@RequestParam String id) {
		return alarmDealLogService.selectDetailById(id);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<AlarmDealLog> list = alarmDealLogService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
	public ModelAndView getList(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, String> param = new HashMap<>();
		for (Map.Entry index : parameterMap.entrySet()){
			String key = Optional.ofNullable(index.getKey()).map(Object::toString).orElse("");
			String value = Optional.ofNullable(index.getValue()).map(Object::toString).orElse("");
			param.put(key, value);
		}
		List<Map<String, String>> list = alarmDealLogService.queryForPage(param);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
