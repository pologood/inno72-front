package com.inno72.service.impl;

import com.inno72.common.json.JsonUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.BeideMaClient;
import okhttp3.MediaType;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class BeideMaClientImpl implements BeideMaClient{
    private static final Logger LOGGER = LoggerFactory.getLogger(BeideMaClientImpl.class);
    @Autowired
    private IRedisUtil iRedisUtil;
    @Value("${env}")
    private String env;
    private String PRINCIPAL_REDIS_KEY = "bedema:principal";
    @Override
    public String shipment(String openId, String orderNo, Integer isSuccess) {
        try {
            String principal = getPrincipal();
            //调用贝德玛出货接口
            Map<String,Object> datajson = new HashMap<String,Object>();
            datajson.put("OpenId",openId);
            datajson.put("OrderNo",orderNo);
            datajson.put("IsSuccess",isSuccess);
            Map<String,String> request = new HashMap<String,String>();
            request.put("requestEntity",JsonUtil.toJson(datajson));
            LOGGER.info("SendCallBack request = {}",JsonUtil.toJson(request));
            Map<String,String> header = new HashMap<String,String>();
            header.put("Principal",principal);
            String response = HttpClient.form(getDomain()+"/VendingMachine/SendCallBack",request,header);
//            String response = HttpClient.post(getDomain()+"/VendingMachine/SendCallBack", MediaType.parse("application/json; charset=utf-8"), JsonUtil.toJson(map), HttpClient.Request_Type.String, HttpClient.Response_Type.String, header).toString();
            LOGGER.info("SendCallBack response = {}",response);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
            throw new RuntimeException();
        }
        return null;
    }

    private String getPrincipal() throws IOException {
        //从缓存获取
        String principal = iRedisUtil.get(PRINCIPAL_REDIS_KEY);
        if(StringUtils.isEmpty(principal)){
            synchronized (this){
                if(StringUtils.isEmpty(principal)){
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("username","VendingMachine");
                    map.put("password","Machine@123cn");
                    LOGGER.info("getPrincipal request = {}",JsonUtil.toJson(map));
                    Response response =HttpClient.form(getDomain()+"/Common/Login",map);
                    principal = response.header("Principal");
                    LOGGER.info("getPrincipal response = {},principal={}",response.body().string(),principal);
                    iRedisUtil.setex(PRINCIPAL_REDIS_KEY,2*60*60*1000,principal);
                }
            }
        }
        return principal;
    }

    private String getDomain() {
        switch(env){
            case "prod": return "http://wechat.bioderma.net.cn/API/";
            default: return "http://wechat-UAT.bioderma.net.cn:8020/API/";
        }
    }

}