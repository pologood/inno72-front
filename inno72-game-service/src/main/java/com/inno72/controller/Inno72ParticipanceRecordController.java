package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72ParticipanceRecord;
import com.inno72.service.Inno72ParticipanceRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/06/28.
*/
@RestController
@RequestMapping("/inno72/participance/record")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72ParticipanceRecordController {
    @Resource
    private Inno72ParticipanceRecordService inno72ParticipanceRecordService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(Inno72ParticipanceRecord inno72ParticipanceRecord) {
        inno72ParticipanceRecordService.save(inno72ParticipanceRecord);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam Integer id) {
        inno72ParticipanceRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(Inno72ParticipanceRecord inno72ParticipanceRecord) {
        inno72ParticipanceRecordService.update(inno72ParticipanceRecord);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam Integer id) {
        Inno72ParticipanceRecord inno72ParticipanceRecord = inno72ParticipanceRecordService.findById(id);
        return ResultGenerator.genSuccessResult(inno72ParticipanceRecord);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Inno72ParticipanceRecord> list = inno72ParticipanceRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
