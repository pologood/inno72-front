package com.inno72.controller;

import com.inno72.common.Inno72BizException;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.service.Inno72NewretailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Inno72NewretailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72NewretailController.class);
    @Autowired
    private Inno72NewretailService service;
    /**
     * 查找门店id
     */
    @RequestMapping(value = "/findStores")
    public Result<Object> findStoreId(String sessionKey, String storeName) {
        try{
            Long storeId = service.findStores(sessionKey,storeName);
            return Results.success(storeId);
        }catch(Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch(Exception e){
            LOGGER.error("findStoreId",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/saveDevice",method = RequestMethod.POST)
    public Result<Object> saveDevice(String sessionKey, String deviceName, Long storeId, String osType, String deviceType, String outerCode) {
        try{
            String deviceCode = service.saveDevice(sessionKey,deviceName,storeId,osType,deviceType,outerCode);
            return Results.success(deviceCode);
        }catch(Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch(Exception e){
            LOGGER.error("saveDevice",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/getMemberIdentity")
    public Result<Object> getMemberIdentity(String sessionKey, String mixNick) {
        try{
            boolean flag =  service.getMemberIdentity(sessionKey,mixNick);
            return Results.success(flag);
        }catch(Exception e){
            LOGGER.error("getMemberIdentity",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/getStoreMemberurl")
    public Result<Object> getStoreMemberurl(String sessionKey, String deviceCode) {
        try{
            String url =  service.getStoreMemberurl(sessionKey,deviceCode);
            return Results.success(url);
        }catch(Exception e){
            LOGGER.error("getStoreMemberurl",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/deviceVendorFeedback")
    public Result<Object> deviceVendorFeedback(String sessionKey, String tradeNo,
                                               String tradeType, String deviceCode, String action
            , String itemId, String couponId, String userNick, String outerBizId, String opTime, String outerUser)  {
        try{
            service.deviceVendorFeedback(sessionKey,tradeNo,tradeType,deviceCode,action,
                    itemId,couponId,userNick,outerBizId,opTime,outerUser);
            return Results.success();
        }catch(Exception e){
            LOGGER.error("deviceVendorFeedback",e);
            return Results.failure("系统异常");
        }
    }

}
