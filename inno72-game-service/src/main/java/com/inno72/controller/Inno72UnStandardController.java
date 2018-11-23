package com.inno72.controller;

import com.google.gson.Gson;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.service.Inno72WeChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 非标准接口
 */
@RestController
@RequestMapping(value = "/api/unstandard")
public class Inno72UnStandardController {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Resource
    private Inno72WeChatService inno72WeChatService;
    /**
     * 获取微信二维码列表
     */
    @ResponseBody
    @RequestMapping(value = "/findWeChatQrCodes", method = {RequestMethod.GET})
    public Result<Object> findWeChatQrCodes(String activityId) {
        try{
            List<String> list = inno72WeChatService.findWeChatQrCodes(activityId);
            return Results.success(list);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }



    @RequestMapping(value = "/getWeChat")
    public String getWeChat(String url,String code,String appid){
        try{
            //
            return inno72WeChatService.findWeChatUserInfo(url,code,appid);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
