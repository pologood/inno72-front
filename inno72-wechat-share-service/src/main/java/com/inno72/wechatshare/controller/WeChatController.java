package com.inno72.wechatshare.controller;

import com.inno72.wechatshare.common.Result;
import com.inno72.wechatshare.common.Results;
import com.inno72.wechatshare.utils.wechat.WeinXinUtil;
import com.inno72.wechatshare.utils.wechat.WinXinEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeChatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatController.class);
    @Autowired
    private WeinXinUtil util;
    @Value("${aa}")
    private String aa;

    @RequestMapping
    public Result getWeChat(String url){
        try{
            WinXinEntity wx = util.getWinXinEntity(url);
            return Results.success(wx);
        }catch(Exception e){
            LOGGER.error("getWeChat",e);
            return Results.failure(e.getMessage());
        }
    }

    @RequestMapping("/test")
    public String test(){
        return aa;
    }
}
