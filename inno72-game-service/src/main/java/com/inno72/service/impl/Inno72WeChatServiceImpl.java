package com.inno72.service.impl;

import com.inno72.common.Inno72BizException;
import com.inno72.mapper.Inno72InteractShopsMapper;
import com.inno72.mapper.Inno72MerchantMapper;
import com.inno72.model.Inno72Merchant;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72WeChatService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class Inno72WeChatServiceImpl implements Inno72WeChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72WeChatServiceImpl.class);
    @Autowired
    private Inno72InteractShopsMapper inno72InteractShopsMapper;
    @Autowired
    private Inno72MerchantMapper inno72MerchantMapper;
    @Override
    public List<String> findWeChatQrCodes(String activityId) {
        return inno72InteractShopsMapper.findWeChatQrCodes(activityId);
    }

    @Override
    public String findWeChatUserInfo(String url, String code, String appid) {
        //根据merchantCode查询商家调用接口地址
        Inno72Merchant merchant = inno72MerchantMapper.findByMerchantCode(appid);
        String serverUrl = null;
        if(merchant == null) {
            throw new Inno72BizException("查找商户为空，merchantCode="+appid);
        }else{
            serverUrl = merchant.getWechatUserApiUrl();
        }
//        String serverUrl = "http://api.activity.36solo.com/user";
        if(StringUtils.isEmpty(serverUrl)){
            throw new Inno72BizException("商户微信获取用户信息url配置为空，merchantCode="+appid);
        }
        serverUrl = buildUrl(serverUrl,url,code,appid);
        String result =  HttpClient.get(serverUrl);
        LOGGER.info("调用商户获取用户信息url={}，result={}",serverUrl,result);
        return result;
    }

    private String buildUrl(String serverUrl, String url, String code,String appid) {
        StringBuilder sb = new StringBuilder(serverUrl);
        sb.append("?url=");
        sb.append(url);
        sb.append("&code=");
        sb.append(code);
        sb.append("&appid=");
        sb.append(appid);
        return sb.toString();
    }
}
