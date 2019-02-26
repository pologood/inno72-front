package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Interact;
import com.inno72.service.Inno72InteractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2019/01/02.
*/
@RestController
@RequestMapping("/inno72/interact")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72InteractController {
    @Resource
    private Inno72InteractService inno72InteractService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(Inno72Interact inno72Interact) {
		inno72InteractService.save(inno72Interact);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam String id) {
		inno72InteractService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(Inno72Interact inno72Interact) {
		inno72InteractService.update(inno72Interact);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam String id) {
		Inno72Interact inno72Interact = inno72InteractService.findById(id);
        return ResultGenerator.genSuccessResult(inno72Interact);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Inno72Interact> list = inno72InteractService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
