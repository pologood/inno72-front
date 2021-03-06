package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72ActivityInfoDesc;
import com.inno72.service.Inno72ActivityInfoDescService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2019/01/11.
*/
@RestController
@RequestMapping("/inno72/activity/info/desc")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72ActivityInfoDescController {
    @Resource
    private Inno72ActivityInfoDescService inno72ActivityInfoDescService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(Inno72ActivityInfoDesc inno72ActivityInfoDesc) {
		inno72ActivityInfoDescService.save(inno72ActivityInfoDesc);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam String id) {
		inno72ActivityInfoDescService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(Inno72ActivityInfoDesc inno72ActivityInfoDesc) {
		inno72ActivityInfoDescService.update(inno72ActivityInfoDesc);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam String id) {
		Inno72ActivityInfoDesc inno72ActivityInfoDesc = inno72ActivityInfoDescService.findById(id);
        return ResultGenerator.genSuccessResult(inno72ActivityInfoDesc);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Inno72ActivityInfoDesc> list = inno72ActivityInfoDescService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
