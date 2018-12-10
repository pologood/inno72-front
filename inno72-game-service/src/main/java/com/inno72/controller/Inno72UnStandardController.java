package com.inno72.controller;

import com.google.gson.Gson;
import com.inno72.common.Inno72BizException;
import com.inno72.common.RedisConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72UnStandardService;
import com.inno72.service.Inno72WeChatService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Resource
    private IRedisUtil iRedisUtil;
    @Resource
    private Inno72UnStandardService inno72UnStandardService;
    @Value("${phoneverificationcode_limit_time}")
    private Integer phoneverificationcodeLimitTime;
    @Value("${phoneverificationcode_limit_times}")
    private Integer phoneverificationcodeLimitTimes;
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

    /**
     * 获取手机号验证码
     */
    @ResponseBody
    @RequestMapping(value = "/getPhoneVerificationCode", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<Object> getPhoneVerificationCode(String sessionUuid,String phone) {
        try{
            //检查
            checkParam(sessionUuid,phone);

            //检查redis
            String key = RedisConstants.PHONEVERIFICATIONCODE_TIME_LIMIT_REDIS_KEY+sessionUuid +":"+phone;
            if(iRedisUtil.exists(key)){
                Long time = iRedisUtil.ttl(key);
                return Results.warn("60s 内只能发一次" ,1,time);
            }
            //10分钟内只能发三次
            key = RedisConstants.PHONEVERIFICATIONCODE_TIMES_LIMIT_REDIS_KEY+sessionUuid +":"+phone;
            if(iRedisUtil.exists(key)&&Integer.parseInt((String)iRedisUtil.get(key))>=phoneverificationcodeLimitTimes){
                Long time = iRedisUtil.ttl(key);
                return Results.warn(phoneverificationcodeLimitTime+"分钟内只能发"+phoneverificationcodeLimitTimes+"次" ,1,60);
            }
            inno72UnStandardService.getPhoneVerificationCode(sessionUuid,phone);
            return Results.success(60);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }

    private void checkParam(String sessionUuid, String phone){
        if(StringUtils.isEmpty(sessionUuid) || StringUtils.isEmpty(phone)){
            LOGGER.error("参数异常 sessionUuid={},phone={}");
            throw new Inno72BizException("参数异常");
        }
    }

}
