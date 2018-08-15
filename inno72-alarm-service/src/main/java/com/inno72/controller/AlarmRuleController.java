package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
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
import com.inno72.model.AlarmRule;
import com.inno72.service.AlarmRuleService;
import com.inno72.vo.AlarmRuleRequestVo;

/**
 * Created by CodeGenerator on 2018/08/13.
 */
@RestController
@RequestMapping("/alarm/rule")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AlarmRuleController {
	@Resource
	private AlarmRuleService alarmRuleService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result add(AlarmRule alarmRule) {
		alarmRuleService.save(alarmRule);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String> save(@RequestBody String json){

		return alarmRuleService.addOrUpdate(json);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result delete(@RequestParam String id) {
		alarmRuleService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result update(AlarmRule alarmRule) {
		alarmRuleService.update(alarmRule);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result detail(@RequestParam String id) {
		AlarmRuleRequestVo alarmRule = alarmRuleService.queryById(id);
		return ResultGenerator.genSuccessResult(alarmRule);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<AlarmRule> list = alarmRuleService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}


	@RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
	public ModelAndView getList(AlarmRule alarmRule) {
		List<AlarmRule> list = alarmRuleService.queryForPage(alarmRule);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

}
