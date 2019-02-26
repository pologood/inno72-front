package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Store;
import com.inno72.service.Inno72StoreService;
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
@RequestMapping("/inno72/store")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72StoreController {
    @Resource
    private Inno72StoreService inno72StoreService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(Inno72Store inno72Store) {
		inno72StoreService.save(inno72Store);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam String id) {
		inno72StoreService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(Inno72Store inno72Store) {
		inno72StoreService.update(inno72Store);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam String id) {
		Inno72Store inno72Store = inno72StoreService.findById(id);
        return ResultGenerator.genSuccessResult(inno72Store);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Inno72Store> list = inno72StoreService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/all", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result all() {
        return inno72StoreService.selectAll();
    }
}
