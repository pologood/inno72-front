package com.github.binarywang.demo.wx.mp.service.impl;

import com.github.binarywang.demo.wx.mp.service.GameServcie;
import com.github.binarywang.demo.wx.mp.utils.FastJsonUtils;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import com.github.binarywang.demo.wx.mp.vo.Result;
import com.google.gson.Gson;
import com.inno72.plugin.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameServiceImpl implements GameServcie {

    @Value("${gameserver}")
    private String gameServerUrl;
//    @Value("${login_url}")
//    private String loginUrl;
    @Value("${refund_url}")
    private String refundUrl;
    @Value("${error_page_url}")
    private String errorPageUrl;
    @Override
    public String redirectAdapter(WxMpUser user) {
        Gson gson = new Gson();
        String result =  HttpClient.post(gameServerUrl+"/joinPhoneFlag",gson.toJson(user));
        log.info("result = {}",result);
        try{
            Result res = gson.fromJson(result, Result.class);
            if(res.getCode() == Result.SUCCESS){
                if(res.getData() != null){
                    String gameUserId = (String)res.getData();
                    return refundUrl+"?gameUserId="+gameUserId;
                }else{
//                    return loginUrl+"?openId="+user.getOpenId();
                    //其他扫描暂时不处理
                    return errorPageUrl;
                }
            }else{
                log.error("joinPhoneFlag error msg = {}",res.getMsg());
            }
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }
}
