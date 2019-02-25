package com.inno72.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.common.*;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.util.SignUtil;
import com.inno72.common.util.UuidUtil;
import com.inno72.model.Inno72Order;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72AuthInfoService;
import com.inno72.service.Inno72BeidemaService;
import com.inno72.service.Inno72OrderService;
import com.inno72.service.PointService;
import com.inno72.vo.AuthInfoWechatVo;
import com.inno72.vo.BeiDeMaRequestVo;
import com.inno72.vo.Inno72MachineInformation;
import com.inno72.vo.UserSessionVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/beidema")
public class Inno72BeiDeMaController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Integer NONCE_EXPIRE_TIME = 10*60*1000;
    @Autowired
    private IRedisUtil iRedisUtil;
    @Autowired
    private GameSessionRedisUtil gameSessionRedisUtil;
    @Autowired
    private PointService pointService;
    @Autowired
    private Inno72AuthInfoService inno72AuthInfoService;
    @Autowired
    private Inno72BeidemaService inno72BeidemaService;
    @Autowired
    private Inno72OrderService inno72OrderService;
    private static String NONCE_REDISKEY = "common:nonce:";


    @RequestMapping(value = "/scanQrCode", method = {RequestMethod.GET,RequestMethod.POST})
    public Result loginRedirect(@RequestBody BeiDeMaRequestVo vo) {
        try {
            checkSign(vo);
            if(StringUtils.isEmpty(vo.getMachentCode())){
                throw new Inno72BizException("参数缺失");
            }
            synchronized (this) {
                UserSessionVo sessionVo = gameSessionRedisUtil.getSessionKey(vo.getMachineCode());
                boolean qrCode = gameSessionRedisUtil.exists(vo.getMachineCode() + "qrCode");
                logger.info("loginRedirect qrCode is {}", qrCode);
                if (!qrCode) {
                    sessionVo.setIsScanned(false);
                }

                if (sessionVo.getIsScanned()) {
                    logger.info("loginRedirect 二维码已经被扫描 machineCode = {}",vo.getMachentCode());
                    return Results.warn("二维码已经被扫描",Result.SUCCESS,1);
                }else{

                    AuthInfoWechatVo authInfo = new AuthInfoWechatVo();
                    authInfo.setChannelType(2);
                    authInfo.setAppId(BeidemaConstants.appId);
                    authInfo.setNickname("beidema");
                    authInfo.setOpenId(vo.getOpenid());
                    Result r = inno72AuthInfoService.processBeforeLogged(vo.getMachineCode(), JsonUtil.toJson(authInfo), UuidUtil.getUUID32());
                    if(r.getCode() != Result.SUCCESS){
                        return r;
                    }
                    Boolean loginflag = inno72AuthInfoService.setLogged(vo.getMachineCode());
                    if(!loginflag){
                        Results.failure("用户已经登录，不能继续登录");
                    }
                    sessionVo.setIsScanned(true);
                    // 设置15秒内二维码不能被扫
                    gameSessionRedisUtil.setSessionEx(vo.getMachineCode() + "qrCode", vo.getMachineCode(), 15);
                    pointService.innerPoint(JSON.toJSONString(sessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.SCAN_LOGIN);
                    Results.success(0);
                }
            }
            return Results.success();
        }catch (Inno72BizException e){
            logger.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/order", method = {RequestMethod.GET,RequestMethod.POST})
    public Result order(@RequestBody BeiDeMaRequestVo vo) {
        try {
            checkSign(vo);
            if(StringUtils.isEmpty(vo.getMachentCode())){
                throw new Inno72BizException("参数缺失");
            }
            return inno72BeidemaService.order(vo);
        }catch (Inno72BizException e){
            logger.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/pay", method = {RequestMethod.GET,RequestMethod.POST})
    public Result pay(@RequestBody BeiDeMaRequestVo vo) {
        try {
            checkSign(vo);
            if(StringUtils.isEmpty(vo.getMachentCode())){
                throw new Inno72BizException("参数缺失");
            }
            inno72OrderService.updateOrderStatusAndPayStatusByRefOrderNum(vo.getOrderNum(),Inno72Order.INNO72ORDER_ORDERSTATUS.PAY.getKey(),Inno72Order.INNO72ORDER_PAYSTATUS.SUCC.getKey());
            return Results.success(0);
        }catch (Inno72BizException e){
            logger.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return Results.failure("系统异常");
        }
    }


    @RequestMapping(value = "/getSign", method = {RequestMethod.GET,RequestMethod.POST})
    public Result getSign(@RequestBody BeiDeMaRequestVo vo) {
        try {
            String sign = SignUtil.makeSign(vo, MachentSecureKeyCache.getSecureKey(vo.getMachentCode()));
            return Results.success(sign);
        }catch (Inno72BizException e){
            logger.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return Results.failure("系统异常");
        }
    }

    private void checkSign(BeiDeMaRequestVo vo) throws IllegalAccessException {

        if(vo.getTimestamp() == null || StringUtils.isEmpty(vo.getNonce())
                || StringUtils.isEmpty(vo.getMachentCode()) || StringUtils.isEmpty(vo.getSign())){
            throw new Inno72BizException("参数缺失");
        }
        //重放攻击检查
        Long time = vo.getTimestamp();
        Long now = System.currentTimeMillis();
        if((now - time) > NONCE_EXPIRE_TIME){
            throw new Inno72BizException("时间戳超时");
        }

        String key = NONCE_REDISKEY+vo.getNonce();
        Boolean flag = iRedisUtil.exists(key);
        if(flag){
            throw new Inno72BizException("重复调用异常");
        }
        iRedisUtil.setex(key,NONCE_EXPIRE_TIME,"1");

        //签名检查
        String sign = SignUtil.makeSign(vo, MachentSecureKeyCache.getSecureKey(vo.getMachentCode()));
        if(!sign.equals(vo.getSign())){
            throw new Inno72BizException("签名错误");
        }
    }
}
