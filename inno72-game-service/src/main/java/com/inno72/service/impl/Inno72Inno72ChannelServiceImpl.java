package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.inno72.common.*;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.*;
import com.inno72.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service("INNO72")
@Transactional
public class Inno72Inno72ChannelServiceImpl implements Inno72ChannelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72Inno72ChannelServiceImpl.class);

    @Autowired
    private Inno72GameServiceProperties inno72GameServiceProperties;

    @Autowired
    private Inno72GameUserChannelService inno72GameUserChannelService ;

    @Autowired
    private PointService pointService;

    @Autowired
    private Inno72MerchantMapper inno72MerchantMapper;

    @Autowired
    private Inno72ChannelMapper inno72ChannelMapper;

    @Autowired
    private Inno72OrderMapper inno72OrderMapper;

    @Autowired
    private Inno72GameUserChannelMapper inno72GameUserChannelMapper;

    @Autowired
    private Inno72GameUserMapper inno72GameUserMapper;

    @Autowired
    private Inno72LocaleMapper inno72LocaleMapper;

    @Autowired
    private Inno72MachineMapper inno72MachineMapper;

    @Autowired
    private Inno72InteractService inno72InteractService;

    @Autowired
    private Inno72GameApiService inno72GameApiService;


    @Autowired
    private Inno72GameMapper inno72GameMapper;

    @Autowired
    private Inno72GameUserLifeMapper inno72GameUserLifeMapper;

    @Autowired
    private Inno72AuthInfoService inno72AuthInfoService;

    @Autowired
    private Inno72MachineService inno72MachineService;

    @Autowired
    private Inno72GameUserLoginMapper inno72GameUserLoginMapper;

    @Value("${env}")
    private String env;

    @Value("${duduji.appid}")
    private String dudujiAppId;

    @Override
    public String buildQrContent(Inno72Machine inno72Machine, String sessionUuid,StandardPrepareLoginReqVo req) {
        String redirect = inno72GameServiceProperties.get("loginRedirect");
        String ext = req.getExt();
        String PU = FastJsonUtils.getString(ext, "PU");
        String tmalFlag = FastJsonUtils.getString(ext, "tmalFlag");
        String url = buildUrl(redirect,sessionUuid,PU,tmalFlag);
        return url;
    }

    @Override
    public Result<Object> paiYangProcessBeforeLogged(String sessionUuid, UserSessionVo sessionVo, String authInfo, String traceId) {
        String userId = FastJsonUtils.getString(authInfo, "phone");
        String nickName = userId.substring(0, 3) + "****" + userId.substring(7, userId.length());
        String merchentCode = sessionVo.getSellerId();
        Inno72Merchant inno72Merchant = null;
        if(!StringUtils.isEmpty(merchentCode)){
            inno72Merchant= inno72MerchantMapper.findByMerchantCode(merchentCode);
        }
        if(inno72Merchant == null){
            LOGGER.error("inno72Merchant is null merchentCode = {}",merchentCode);
            return Results.failure("参数错误！");
        }
        String mid = sessionVo.getMachineId();
        Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(mid);
        if (inno72Machine == null) {
            return Results.failure("机器错误！");
        }
        Inno72Interact interact = inno72InteractService.findById(sessionVo.getInno72MachineVo().getActivityId());
        String gameId = sessionVo.getInno72MachineVo().getInno72Games().getId();
        if (StringUtil.isEmpty(gameId)) {
            return Results.failure("没有绑定的游戏！");
        }
        Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
        if (inno72Game == null) {
            return Results.failure("不存在的游戏！");
        }

        String channelId = inno72Merchant.getChannelId();
        Inno72Channel inno72Channel = inno72ChannelMapper.selectByPrimaryKey(channelId);

        Map<String, String> userChannelParams = new HashMap<>();
        userChannelParams.put("channelId", channelId);
        userChannelParams.put("channelUserKey", userId);
        Inno72GameUserChannel userChannel = inno72GameUserChannelMapper.selectByChannelUserKey(userChannelParams);

        if (userChannel == null) {
            Inno72GameUser inno72GameUser = new Inno72GameUser();
            inno72GameUserMapper.insert(inno72GameUser);
            LOGGER.info("插入游戏用户表 完成 ===> {}", JSON.toJSONString(inno72GameUser));
            userChannel = new Inno72GameUserChannel(nickName, userId, channelId, inno72GameUser.getId(),
                    inno72Channel.getChannelName(), userId, null,StandardLoginTypeEnum.INNO72.getValue());
            userChannel.setChannelId(channelId);
            inno72GameUserChannelMapper.insert(userChannel);
            LOGGER.info("插入游戏用户渠道表 完成 ===> {}", JSON.toJSONString(userChannel));
        }

        //关联微信
        String code = FastJsonUtils.getString(authInfo, "code");
        if(!StringUtils.isEmpty(code)){
            joinPhoneWechatUser(code,userChannel.getGameUserId());
        }

        //插入gameLife表
        Inno72Locale inno72Locale = inno72LocaleMapper.selectByPrimaryKey(inno72Machine.getLocaleId());
        Inno72GameUserLife life = new Inno72GameUserLife(userChannel == null ? null : userChannel.getGameUserId(),
                userChannel == null ? null : userChannel.getId(), inno72Machine.getMachineCode(),
                userChannel == null ? null : userChannel.getUserNick(), interact.getId(),
                interact.getName(), interact.getId(), inno72Game.getId(), inno72Game.getName(),
                inno72Machine.getLocaleId(), inno72Locale == null ? "" : inno72Locale.getMall(), null, "", null, null,
                userId, sessionVo.getSellerId() == null ? inno72Merchant.getMerchantCode() : sessionVo.getSellerId(), sessionVo.getGoodsCode() == null ? "" : sessionVo.getGoodsCode());
        life.setChannelId(channelId);
        LOGGER.info("插入用户游戏记录 ===> {}", JSON.toJSONString(life));
        inno72GameUserLifeMapper.insert(life);

        //插入登陆日志
        Inno72GameUserLogin inno72GameUserLogin = new Inno72GameUserLogin();
        String operatingSystem = FastJsonUtils.getString(authInfo, "operatingSystem");
        String phoneModel = FastJsonUtils.getString(authInfo, "phoneModel");
        String scanSoftware = FastJsonUtils.getString(authInfo, "scanSoftware");
        if(!StringUtils.isEmpty(operatingSystem)){
            inno72GameUserLogin.setOperatingSystem(Integer.parseInt(operatingSystem));
        }
        inno72GameUserLogin.setClientInfo(FastJsonUtils.getString(authInfo, "clientInfo"));
        inno72GameUserLogin.setPhoneModel(phoneModel);
        inno72GameUserLogin.setScanSoftware(scanSoftware);
        inno72GameUserLogin.setActivityId(sessionVo.getActivityId());
        inno72GameUserLogin.setLocaleId(inno72Machine.getLocaleId());
        inno72GameUserLogin.setLoginTime(new Date());
        inno72GameUserLogin.setMachineId(inno72Machine.getId());
        inno72GameUserLogin.setProcessed(Inno72GameUserLogin.PROCESSED_NO);
        inno72GameUserLogin.setUserId(userChannel.getGameUserId());
        inno72GameUserLogin.setChannelId(channelId);
        inno72GameUserLoginMapper.insert(inno72GameUserLogin);
        sessionVo.setGameUserLoginId(inno72GameUserLogin.getId());
        sessionVo.setUserNick(nickName);
        sessionVo.setUserId(userId);
        sessionVo.setGameUserId(userChannel.getGameUserId());
        sessionVo.setGameId(gameId);
        sessionVo.setSessionUuid(sessionUuid);
        sessionVo.setActivityPlanId(interact.getId());
        boolean canOrder = inno72AuthInfoService.findCanOrder(interact,sessionVo,userChannel.getGameUserId());
        sessionVo.setCanOrder(canOrder);
        if(sessionVo.getGoodsType()!=null && UserSessionVo.GOODSTYPE_COUPON.compareTo(sessionVo.getGoodsType())==0){
            sessionVo.setCountGoods(true);
        }else{
            String goodsId = sessionVo.getGoodsId();
            if(!StringUtils.isEmpty(goodsId)){
                Integer goodsCount = inno72MachineService.getMachineGoodsCount(sessionVo.getGoodsId(),inno72Machine.getId());
                sessionVo.setCountGoods(goodsCount>0);
            }else{
                LOGGER.info("goodsId is null 无法计算CountGoods");
            }
        }
        sessionVo.setChannelId(channelId);
        sessionVo.setActivityId(interact.getId());
        CommonBean.logger(
                CommonBean.POINT_TYPE_LOGIN,
                inno72Machine.getMachineCode(),
                "用户" + nickName + "，登录机器 ["+inno72Machine.getMachineCode()+"], 当前活动 ["+ interact.getName() +"]",
                interact.getId()+"|"+userId);

        return Results.success();
    }

    /**
     * 关联手机号和微信用户
     * @param code
     * @param gameUserId
     */
    private void joinPhoneWechatUser(String code, String gameUserId) {
        //根据code获取微信用户
        WxMpUser user = inno72GameUserChannelService.getWeChatUserByCode(code);
        if(user != null){
            user.setAppId(dudujiAppId);
            Inno72GameUserChannel gameUserChannel = inno72GameUserChannelService.findWeChatUser(user.getUnionId());
            if(gameUserChannel == null){
                inno72GameUserChannelService.saveWechatUser(user,gameUserId);
            }else{
                if(!gameUserChannel.getGameUserId().equals(gameUserId)){
                    inno72GameUserChannelService.updateWechatUser(user,gameUserChannel.getId(),gameUserId);
                }
            }
        }else{
            LOGGER.error("根据code={},查询微信用户为空",code);
            throw new Inno72BizException("无法获取微信用户");
        }
    }



    @Override
    public Result<Object> order(UserSessionVo userSessionVo, String itemId, String inno72OrderId) {
        // 如果有支付链接则返回支付链接
        Map<String, Object> map = new HashMap<>();

        BigDecimal orderPrice = userSessionVo.getOrderPrice();
        if(orderPrice!=null&&orderPrice.compareTo(BigDecimal.ZERO) == 1){
            //需要支付
            String payUrl = inno72GameServiceProperties.get("payUrl");
            map.put("payUrl", null);
        }
        map.put("channelCode",Inno72Channel.CHANNELCODE_INNO72);
        map.put("needPay", false);
        map.put("inno72OrderId", inno72OrderId);
        return Results.success(map);
    }

    private String buildUrl(String redirect,String sessionUuid,String PU,String tmalFlag){
        String url = "";
        if(StringUtils.isEmpty(PU)){
            url = String.format(
                    "%s/?sessionUuid=%s&env=%s&channelType=%s",
                    redirect, sessionUuid, env, StandardLoginTypeEnum.INNO72.getValue());
        }else{
            url = String.format(
                    "%s/?sessionUuid=%s&env=%s&channelType=%s&PU=%s",
                    redirect, sessionUuid, env, StandardLoginTypeEnum.INNO72.getValue(),PU);
        }
        if(!StringUtils.isEmpty(tmalFlag)){
            url+="&tmalFlag="+tmalFlag;
        }
        return url;
    }

    @Override
    public Result<Object> orderPolling(UserSessionVo userSessionVo, MachineApiVo vo) {
        String orderId = userSessionVo.getInno72OrderId();
        Inno72Order order = inno72OrderMapper.selectByPrimaryKey(orderId);
        Map<String, Object> result = new HashMap<>();
        boolean model = false;
        if(Inno72Order.INNO72ORDER_ORDERSTATUS.PAY.getKey() == order.getOrderStatus()){
            String goodsId = userSessionVo.getGoodsId();
            model = true;
            List<String> goodsIds = new ArrayList<>();
            goodsIds.add(goodsId);
            inno72GameApiService.setChannelInfo(userSessionVo, result, goodsIds);
            pointService.innerPoint(JSON.toJSONString(userSessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.PAY);
        }
        result.put("model", model);
        return Results.success(result);
    }
}
