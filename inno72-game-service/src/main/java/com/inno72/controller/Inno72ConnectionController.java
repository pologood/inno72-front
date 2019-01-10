package com.inno72.controller;

import com.inno72.common.Inno72BizException;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72ConnectionService;
import com.inno72.vo.PushRequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/connection")
public class Inno72ConnectionController {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Inno72ConnectionService inno72ConnectionService;

    /**
     * 申请退款
     */
    @ResponseBody
    @RequestMapping(value = "/changeActivity", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<Object> changeActivity(String machineCode, String activityId, Integer activityType) {
        try{
            LOGGER.info("changeActivity machineCode = {},activityId={},activityType={}",machineCode,activityId,activityType);
            inno72ConnectionService.changeActivity(machineCode,activityId,activityType);
            return Results.success();
        }catch (Inno72BizException e){
            return Results.warn(e.getMessage(),-1);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }
    @Autowired
    Inno72GameServiceProperties inno72GameServiceProperties;
    /**
     * 申请退款
     */
    @ResponseBody
    @RequestMapping(value = "/callBack", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<Object> callBack(String machineCode, String activityId, Integer type,Long version) {
        try{
            LOGGER.info("callBack machineCode = {},activityId={},type={},version={}",machineCode,activityId,type,version);
//            inno72ConnectionService.callBack(machineCode,activityId,type,version);
            //发送长连接
            String pushServerUrl = inno72GameServiceProperties.get("pushServerUrl")+"/pusher/push/one";
            PushRequestVo vo = new PushRequestVo();
            Map map = new HashMap();
            map.put("machineCode",machineCode);
            map.put("activityId",activityId);
            map.put("type",type);
            map.put("version",version);
            vo.setData(JsonUtil.toJson(map));
            vo.setTargetCode(machineCode);
            String request = JsonUtil.toJson(vo);
            LOGGER.info("send msg url = {},data={}",pushServerUrl,request);
            String response = HttpClient.post(pushServerUrl,request);
            LOGGER.info("send msg response={}",response);
            return Results.success();
        }catch (Inno72BizException e){
            return Results.warn(e.getMessage(),-1);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }
}
