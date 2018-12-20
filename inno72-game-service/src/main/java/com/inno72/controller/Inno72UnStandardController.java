package com.inno72.controller;

import com.inno72.common.Inno72BizException;
import com.inno72.common.RedisConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72UnStandardService;
import com.inno72.service.Inno72WeChatService;
import com.inno72.vo.OrderVo;
import com.inno72.vo.UserSessionVo;
import com.inno72.vo.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.awt.*;
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
    public Result<Object> getPhoneVerificationCode(@RequestParam(required = false) String sessionUuid,String phone,@RequestParam(required = false) Integer type) {
        try{
            LOGGER.info("getPhoneVerificationCode sessionUuid = {},phone = {},type = {}",sessionUuid,phone,type);
            //检查redis
            String key = RedisConstants.PHONEVERIFICATIONCODE_TIME_LIMIT_REDIS_KEY +phone;
            if(iRedisUtil.exists(key)){
                Long time = iRedisUtil.ttl(key);
                return Results.warn("60s 内只能发一次" ,1,time);
            }
            if(type == null){
                UserSessionVo sessionVo = new UserSessionVo(sessionUuid);
                String activityId = sessionVo.getActivityId();
                //10分钟内只能发三次
                key = RedisConstants.PHONEVERIFICATIONCODE_TIMES_LIMIT_REDIS_KEY+activityId +":"+phone;
            }else{
                key = RedisConstants.PHONEVERIFICATIONCODE_TIMES_LIMIT_REDIS_KEY+phone;
            }
            if(iRedisUtil.exists(key)&&Integer.parseInt((String)iRedisUtil.get(key))>=phoneverificationcodeLimitTimes){
                Long time = iRedisUtil.ttl(key);
                return Results.warn(phoneverificationcodeLimitTime+"分钟内只能发"+phoneverificationcodeLimitTimes+"次" ,1,60);
            }
            inno72UnStandardService.getPhoneVerificationCode(sessionUuid,phone,type);
            return Results.success(60);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }

    /**
     * 获取手机号验证码
     */
    @ResponseBody
    @RequestMapping(value = "/checkPhoneVerificationCode", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<Object> checkPhoneVerificationCode(@RequestParam(required = false) Integer type,
                                                     @RequestParam(required = false) String openId,
                                                     @RequestParam(required = false) String sessionUuid,
                                                     String phone,@RequestParam(required = false) String verificationCode,
                                                     @RequestParam(required = false) Integer operatingSystem,
                                                     @RequestParam(required = false) String phoneModel,
                                                     @RequestParam(required = false) String sacnSoftware,
                                                     @RequestParam(required = false) String clientInfo) {
        try{
            String gameUserId = inno72UnStandardService.checkPhoneVerificationCode(sessionUuid,phone,verificationCode,operatingSystem,phoneModel,sacnSoftware,clientInfo,type,openId);
            return Results.success(gameUserId);
        }catch (Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }

    /**
     * 选择支付方式
     */
    @ResponseBody
    @RequestMapping(value = "/changePayType", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<Object> changePayType(String sessionUuid,Integer payType) {
        try{
            String payUrl = inno72UnStandardService.changePayType(sessionUuid,payType);
            return Results.success(payUrl);
        }catch (Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }

    /**
     * 选择支付方式
     */
    @ResponseBody
    @RequestMapping(value = "/upfile", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<Object> upfile(String sessionUuid,String photoImg) {
        try{
            inno72UnStandardService.updatePhoto(sessionUuid,photoImg);
            return Results.success();
        }catch (Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }

    /**
     * 支付回调
     */
    @ResponseBody
    @RequestMapping(value = "/payCallback", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<Object> payCallback(String retCode,String billId,String buyerId,String extra,
                                      String fee,String outTradeNo,String spId,Integer terminalType,Integer type) {
        try{
            inno72UnStandardService.payCallback(retCode,billId,buyerId,extra,fee,outTradeNo,spId,terminalType,type);
            return Results.success();
        }catch (Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }

    /**
     * 支付回调
     */
    @ResponseBody
    @RequestMapping(value = "/gamePointTime", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<Object> gamePointTime(String sessionUuid,Integer type) {
        try{
            inno72UnStandardService.gamePointTime(sessionUuid,type);
            return Results.success();
        }catch (Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }

    /**
     * 微信用户关联手机号
     */
    @ResponseBody
    @RequestMapping(value = "/joinPhoneFlag", method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json;charset=UTF-8")
    public Result<Object> joinPhoneFlag(@RequestBody WxMpUser user) {
        try{
            LOGGER.info("user = {}",JsonUtil.toJson(user));
            String gameUserId = inno72UnStandardService.joinPhoneFlag(user);
            return Results.success(gameUserId);
        }catch (Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }

    /**
     * 微信用户关联手机号
     */
    @ResponseBody
    @RequestMapping(value = "/orderList", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<Object> orderList(String gameUserId,Integer pageNum,@RequestParam(required = false,defaultValue = "10")Integer pageSize) {
        try{
            LOGGER.info("orderList gameUserId = {},pageNum = {},pageSize={}",gameUserId,pageNum,pageSize);
            List<OrderVo> list = inno72UnStandardService.orderList(gameUserId,pageNum,pageSize);
            return Results.success(list);
        }catch (Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Results.failure(e.getMessage());
        }
    }

}
