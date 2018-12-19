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

    private static final Integer REDIRECT_FLAG_ORDER = 1;
    private static final Integer REDIRECT_FLAG_JOIN = 0;
    @Value("${login_url}")
    private String loginUrl;
    @Value("${order_url}")
    private String orderUrl;
    @Override
    public String redirectAdapter(WxMpUser user) {
        Gson gson = new Gson();
        String result =  HttpClient.post(gameServerUrl+"/joinPhoneFlag",gson.toJson(user));
        log.info("result = {}",result);
        try{
            Result res = gson.fromJson(result, Result.class);
            if(res.getCode() == Result.SUCCESS){
                Integer flag = Integer.parseInt(FastJsonUtils.getString(result,"data"));
                if(flag == REDIRECT_FLAG_ORDER){
                    return orderUrl;
                }else{
                    return loginUrl;
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
