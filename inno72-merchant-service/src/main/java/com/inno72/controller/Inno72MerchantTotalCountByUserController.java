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
import com.inno72.model.Inno72MerchantTotalCountByUser;
import com.inno72.service.Inno72MerchantTotalCountByUserService;

/**
* Created by CodeGenerator on 2018/11/07.
*/
@RestController
@RequestMapping("/inno72/merchant/total/count/by/user")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72MerchantTotalCountByUserController {
    @Resource
    private Inno72MerchantTotalCountByUserService inno72MerchantTotalCountByUserService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(Inno72MerchantTotalCountByUser inno72MerchantTotalCountByUser) {
inno72MerchantTotalCountByUserService.save(inno72MerchantTotalCountByUser);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam Integer id) {
inno72MerchantTotalCountByUserService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(Inno72MerchantTotalCountByUser inno72MerchantTotalCountByUser) {
inno72MerchantTotalCountByUserService.update(inno72MerchantTotalCountByUser);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam Integer id) {
Inno72MerchantTotalCountByUser inno72MerchantTotalCountByUser = inno72MerchantTotalCountByUserService.findById(id);
        return ResultGenerator.genSuccessResult(inno72MerchantTotalCountByUser);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Inno72MerchantTotalCountByUser> list = inno72MerchantTotalCountByUserService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
