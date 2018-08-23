package com.inno72.machine.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.machine.model.Inno72AdminArea;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.SupplyRequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/machine/machine")
public class MachineController {

    @Resource
    private MachineService machineService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 设置机器点位及管理人
     */
    @RequestMapping(value="set" ,method = {RequestMethod.POST})
    public Result<String> setMachine(@RequestBody SupplyRequestVo vo){
        logger.info("设置机器点位及管理人接口参数：{}",JSON.toJSON(vo));
        Result<String> result = machineService.setMachine(vo);
        logger.info("设置机器点位及管理人接口结果：{}",JSON.toJSON(result));
        return result;
    }

    /**
     * 查询管理的机器
     */
    @RequestMapping(value="list", method = {RequestMethod.POST})
    public Result<List<Inno72Machine>> list(){
        logger.info("查询管理的机器接口");
        Result<List<Inno72Machine>> result = machineService.getMachineList();
        logger.info("查询管理的机器接口结果{}",JSON.toJSON(result));
        return result;

    }

    /**
     * 查询单个一级区域及子区域
     */
    @RequestMapping(value="findAreaByCode", method = {RequestMethod.POST})
    @ResponseBody
    public Result<Inno72AdminArea> findAreaByCode(@RequestBody Inno72AdminArea adminArea){
        logger.info("查询城市及子区域接口");
        return machineService.cityLevelArea(adminArea);
    }


    /**
     * 查询一级区域
     */
    @RequestMapping(value="findFirstLevelArea", method = {RequestMethod.POST})
    public Result<List<Inno72AdminArea>> findFirstLevelArea(){
        logger.info("查询单个一级区域及子区域接口");
        return machineService.findFirstLevelArea();
    }

    /**
     * 查询点位
     */
    @RequestMapping(value="findLocaleByAreaCode", method = {RequestMethod.POST})
    public Result<List<Inno72Locale>> findLocaleByAreaCode(@RequestBody Inno72Locale locale){
        logger.info("查询点位接口参数：{}",JSON.toJSON(locale));
        return machineService.selectLocaleByAreaCode(locale.getAreaCode());
    }

    /**
     * 获取当前点位
     * @param inno72Machine
     * @return
     */
    @RequestMapping(value="getLocale", method = {RequestMethod.POST})
    public Result<Map<String,Object>> getLocale(@RequestBody Inno72Machine inno72Machine){
        logger.info("获取当前点位接口参数：{}",JSON.toJSON(inno72Machine));
        Result<Map<String,Object>> result = machineService.selectMachineLocale(inno72Machine);
        logger.info("获取当前点位接口结果：{}",JSON.toJSON(result));
        return result;
    }


}
