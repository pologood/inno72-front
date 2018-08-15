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
import com.inno72.model.AlarmRuleMsgType;
import com.inno72.service.AlarmRuleMsgTypeService;

/**
* Created by CodeGenerator on 2018/08/13.
*/
@RestController
@RequestMapping("/alarm/rule/msg/type")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AlarmRuleMsgTypeController {
    @Resource
    private AlarmRuleMsgTypeService alarmRuleMsgTypeService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(AlarmRuleMsgType alarmRuleMsgType) {
alarmRuleMsgTypeService.save(alarmRuleMsgType);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam String id) {
alarmRuleMsgTypeService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(AlarmRuleMsgType alarmRuleMsgType) {
alarmRuleMsgTypeService.update(alarmRuleMsgType);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam String id) {
AlarmRuleMsgType alarmRuleMsgType = alarmRuleMsgTypeService.findById(id);
        return ResultGenerator.genSuccessResult(alarmRuleMsgType);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AlarmRuleMsgType> list = alarmRuleMsgTypeService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
