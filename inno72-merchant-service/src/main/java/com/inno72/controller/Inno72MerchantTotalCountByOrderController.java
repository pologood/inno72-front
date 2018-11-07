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
import com.inno72.model.Inno72MerchantTotalCountByOrder;
import com.inno72.service.Inno72MerchantTotalCountByOrderService;

/**
* Created by CodeGenerator on 2018/11/07.
*/
@RestController
@RequestMapping("/inno72/merchant/total/count/by/order")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Inno72MerchantTotalCountByOrderController {
    @Resource
    private Inno72MerchantTotalCountByOrderService inno72MerchantTotalCountByOrderService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(Inno72MerchantTotalCountByOrder inno72MerchantTotalCountByOrder) {
inno72MerchantTotalCountByOrderService.save(inno72MerchantTotalCountByOrder);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam Integer id) {
inno72MerchantTotalCountByOrderService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(Inno72MerchantTotalCountByOrder inno72MerchantTotalCountByOrder) {
inno72MerchantTotalCountByOrderService.update(inno72MerchantTotalCountByOrder);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam Integer id) {
Inno72MerchantTotalCountByOrder inno72MerchantTotalCountByOrder = inno72MerchantTotalCountByOrderService.findById(id);
        return ResultGenerator.genSuccessResult(inno72MerchantTotalCountByOrder);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Inno72MerchantTotalCountByOrder> list = inno72MerchantTotalCountByOrderService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
