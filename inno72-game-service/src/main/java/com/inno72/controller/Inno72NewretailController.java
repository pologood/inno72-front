package com.inno72.controller;

import com.inno72.common.Inno72BizException;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.service.Inno72NewretailService;
import com.inno72.vo.DeviceParamVo;
import com.inno72.vo.DeviceVo;
import com.inno72.vo.MachineVo;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping(value = "/saveMachine",method = RequestMethod.POST)
    public Result<Object> saveMachine(@RequestBody List<DeviceVo> vo) {
        try{
            service.saveMachine(vo);
            return Results.success();
        }catch(Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch(Exception e){
            LOGGER.error("saveMachine",e);
            return Results.failure("系统异常");
        }
    }
    /**
     * 新增机器
     * @param sessionKey
     * @param deviceName 设备名称
     * @param storeId 门店id
     * @param osType 操作系统类型：
     *               WINDOWS("WINDOWS", "WINDOWS"),
     *               ANDROID("ANDROID", "ANDROID"),
     *               IOS("IOS", "IOS"), LINUX("LINUX", "LINUX"), OTHER("OTHER", "OTHER");
     * @param deviceType 设备类型：
     *                   CAMERA("CAMERA", "客流摄像头"), SHELF("SHELF", "云货架"),
     *                   MAKEUP_MIRROR("MAKEUP_MIRROR", "试妆镜"), FITTING_MIRROR("FITTING_MIRROR", "试衣镜"),
     *                   VENDOR("VENDOR", "售货机"), WIFI("WIFI","WIFI探针"), SAMPLE_MACHINE("SAMPLE_MACHINE","派样机"),
     *                   DOLL_MACHINE("DOLL_MACHINE", "娃娃机"), INTERACTIVE_PHOTO("INTERACTIVE_PHOTO", "互动拍照"),
     *                   INTERACTIVE_GAME("INTERACTIVE_GAME", "互动游戏"), USHER_SCREEN("USHER_SCREEN", "智慧迎宾屏"),
     *                   DRESSING("DRESSING", "闪电换装"), MAGIC_MIRROR("MAGIC_MIRROR", "百搭魔镜"),
     *                   SHOES_FITTING_MIRROR("SHOES_FITTING_MIRROR", "试鞋镜"), SKIN_DETECTION("SKIN_DETECTION", "肌肤测试仪"),
     *                   FOOT_DETECTION("FOOT_DETECTION", "测脚仪"),
     *                   RFID_SENSOR("RFID_SENSOR", "RFID"),touch_machine("touch_machine","导购一体屏")
     * @param outerCode  商家自定义设备编码
     */
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
    public Result<Object> getStoreMemberurl(String sessionKey, String deviceCode,String callbackUrl) {
        try{
            String url =  service.getStoreMemberurl(sessionKey,deviceCode,callbackUrl);
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
