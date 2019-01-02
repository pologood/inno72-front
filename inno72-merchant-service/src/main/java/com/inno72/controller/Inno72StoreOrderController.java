package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72StoreOrder;
import com.inno72.service.Inno72StoreOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.vo.Inno72StoreVo;

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
@RequestMapping("/inno72/store/order")
public class Inno72StoreOrderController {
    @Resource
    private Inno72StoreOrderService inno72StoreOrderService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(Inno72StoreOrder inno72StoreOrder) {
		inno72StoreOrderService.save(inno72StoreOrder);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/save", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> save(Inno72StoreVo vo) {

        return inno72StoreOrderService.addOrder(vo);
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam String id) {
		inno72StoreOrderService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(Inno72StoreOrder inno72StoreOrder) {
		inno72StoreOrderService.update(inno72StoreOrder);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam String id) {
		Inno72StoreOrder inno72StoreOrder = inno72StoreOrderService.findById(id);
        return ResultGenerator.genSuccessResult(inno72StoreOrder);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Inno72StoreOrder> list = inno72StoreOrderService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
