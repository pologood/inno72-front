package com.inno72.controller;

import com.inno72.common.Inno72BizException;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.service.Inno72LocalDataSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Inno72LocalDataSendController {
    @Autowired
    private Inno72LocalDataSendService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72LocalDataSendController.class);

    @RequestMapping(value = "/datasend",method = RequestMethod.POST)
    public Result<Object> datasend(@RequestParam(value = "merchantNames") String[] merchantNames) {
        try{
            System.out.println(JsonUtil.toJson(merchantNames));
            service.datasend(merchantNames);
            return Results.success();
        }catch(Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch(Exception e){
            LOGGER.error("saveMachine",e);
            return Results.failure("系统异常");
        }
    }
}
