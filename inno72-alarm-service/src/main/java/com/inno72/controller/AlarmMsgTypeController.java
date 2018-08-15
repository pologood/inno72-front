package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.AlarmMsgType;
import com.inno72.service.AlarmMsgTypeService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/08/13.
 */
@RestController
@RequestMapping("/alarm/msg/type")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AlarmMsgTypeController {
	@Resource
	private AlarmMsgTypeService alarmMsgTypeService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result add(AlarmMsgType alarmMsgType) {
		alarmMsgTypeService.save(alarmMsgType);
		return ResultGenerator.genSuccessResult();
	}
	@RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result delete(@RequestParam String id) {
		alarmMsgTypeService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result update(AlarmMsgType alarmMsgType) {
		alarmMsgTypeService.update(alarmMsgType);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result detail(@RequestParam String id) {
		AlarmMsgType alarmMsgType = alarmMsgTypeService.findById(id);
		return ResultGenerator.genSuccessResult(alarmMsgType);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result list() {
		Condition condition = new Condition( AlarmMsgType.class);
		List<AlarmMsgType> list = alarmMsgTypeService.findByPage(condition);
		return ResultGenerator.genSuccessResult(list);
	}
}
