package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.inno72.common.*;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.service.Inno72AuthInfoService;
import com.inno72.service.Inno72ChannelService;
import com.inno72.service.Inno72InteractMachineTimeService;
import com.inno72.service.Inno72InteractService;
import com.inno72.vo.GoodsVo;
import com.inno72.vo.UserSessionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("WEIXIN")
@Transactional
public class Inno72WeiXinChannelServiceImpl implements Inno72ChannelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72WeiXinChannelServiceImpl.class);

    @Autowired
    private Inno72InteractMachineTimeService inno72InteractMachineTimeService;
    @Autowired
    private Inno72ChannelMapper inno72ChannelMapper;
    @Autowired
    private Inno72GameUserChannelMapper inno72GameUserChannelMapper;
    @Autowired
    private Inno72GameUserMapper inno72GameUserMapper;
    @Autowired
    private Inno72WechatUserAnalyseMapper inno72WechatUserAnalyseMapper;
    @Autowired
    private Inno72InteractService inno72InteractService;
    @Autowired
    private Inno72AuthInfoService inno72AuthInfoService;
    @Autowired
    private Inno72LocaleMapper inno72LocaleMapper;
    @Autowired
    private Inno72MachineMapper inno72MachineMapper;
    @Autowired
    private Inno72GameMapper inno72GameMapper;
    @Autowired
    private Inno72MerchantMapper inno72MerchantMapper;
    @Autowired
    private Inno72GameUserLifeMapper inno72GameUserLifeMapper;
    @Autowired
    private Inno72GameServiceProperties inno72GameServiceProperties;
    @Autowired
    private GameSessionRedisUtil gameSessionRedisUtil;
    @Autowired
    private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

    @Override
    public String buildQrContent(Inno72Machine inno72Machine,String sessionUuid) {
        Inno72InteractMachine interactMachine = inno72InteractMachineTimeService.findActiveInteractMachine(inno72Machine.getMachineCode());
        if(interactMachine == null){
            LOGGER.info("此机器无派样活动配置machineCode={}",inno72Machine.getMachineCode());
            throw new Inno72BizException("此机器无派样活动配置machineCode="+inno72Machine.getMachineCode());
        }
        return buildParam(inno72Machine.getMachineCode(),interactMachine.getInteractId(),sessionUuid);
    }

    @Override
    public Result<Object> paiYangProcessBeforeLogged(String sessionUuid, UserSessionVo sessionVo, String authInfo, String traceId) {

        String mid = sessionVo.getMachineId();
        Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(mid);
        if (inno72Machine == null) {
            return Results.failure("机器错误！");
        }

        String gameId = sessionVo.getInno72MachineVo().getInno72Games().getId();
        String playCode;
        if (StringUtil.isEmpty(gameId)) {
            return Results.failure("没有绑定的游戏！");
        }

        Inno72Game inno72Game = inno72GameMapper.selectByPrimaryKey(gameId);
        if (inno72Game == null) {
            return Results.failure("不存在的游戏！");
        }

        String openId = FastJsonUtils.getString(authInfo, "openId");
        String nickname = FastJsonUtils.getString(authInfo, "nickname");
        String appId = FastJsonUtils.getString(authInfo, "appId");
        String interactId = sessionVo.getInno72MachineVo().getActivityId();
        //渠道信息
        Inno72Channel channel = inno72ChannelMapper.findChannelBySellerId(appId);
        //保存gameUserChannel
        Inno72GameUserChannel gameUserChannel = new Inno72GameUserChannel();
        gameUserChannel.setChannelId(channel.getId());
        gameUserChannel.setChannelUserKey(openId);
        gameUserChannel.setSellerId(appId);
        List<Inno72GameUserChannel> resultList = inno72GameUserChannelMapper.select(gameUserChannel);
        gameUserChannel.setChannelName(channel.getChannelName());
        gameUserChannel.setExt(authInfo);
        gameUserChannel.setUserNick(nickname);
        gameUserChannel.setLoginType(StandardLoginTypeEnum.WEIXIN.getValue());
        if(resultList.size()>0){
            gameUserChannel.setId(resultList.get(0).getId());
            gameUserChannel.setGameUserId(resultList.get(0).getGameUserId());
            inno72GameUserChannelMapper.updateByPrimaryKeySelective(gameUserChannel);
        }else{
            Inno72GameUser inno72GameUser = new Inno72GameUser();
            inno72GameUserMapper.insert(inno72GameUser);
            gameUserChannel.setGameUserId(inno72GameUser.getId());
            gameUserChannel.setCreateTime(LocalDateTime.now());
            LOGGER.info("插入游戏用户表 完成 ===> {}", JSON.toJSONString(inno72GameUser));
            inno72GameUserChannelMapper.insert(gameUserChannel);
            LOGGER.info("插入游戏用户渠道表 完成 ===> {}", JSON.toJSONString(gameUserChannel));
        }

        //插入Inno72WechatUserAnalyse
        Inno72WechatUserAnalyse  inno72WechatUserAnalyse = new Inno72WechatUserAnalyse();
        inno72WechatUserAnalyse.setActivityId(interactId);
        inno72WechatUserAnalyse.setOpenId(openId);
        inno72WechatUserAnalyse.setSellerId(appId);
        List<Inno72WechatUserAnalyse> list = inno72WechatUserAnalyseMapper.select(inno72WechatUserAnalyse);
        if(list.size() == 0){
            inno72WechatUserAnalyse.setCreateTime(new Date());
            inno72WechatUserAnalyse.setGameUserChannelId(gameUserChannel.getId());
            inno72WechatUserAnalyseMapper.insert(inno72WechatUserAnalyse);
        }

        Inno72Interact interact = inno72InteractService.findById(sessionVo.getInno72MachineVo().getActivityId());
        Inno72Merchant inno72Merchant = inno72MerchantMapper.findByMerchantCode(appId);
        sessionVo.setUserNick(nickname);
        sessionVo.setUserId(openId);
        sessionVo.setGameId(gameId);
        sessionVo.setSessionUuid(sessionUuid);
        sessionVo.setActivityPlanId(interact.getId());
        sessionVo.setSellerId(inno72Merchant.getMerchantCode());
        sessionVo.setSellerName(inno72Merchant.getMerchantName());
        sessionVo.setMerchantName(inno72Merchant.getMerchantName());
//        boolean canOrder = inno72AuthInfoService.findCanOrder(interact,sessionVo,gameUserChannel.getId());
        boolean canOrder = true;
        sessionVo.setCanOrder(canOrder);
        sessionVo.setChannelId(channel.getId());
        sessionVo.setActivityId(interact.getId());
        sessionVo.setGameUserId(gameUserChannel.getGameUserId());
        /**
         * 获取此商户的货到商品数量
         */
		List<GoodsVo> goodslist = inno72SupplyChannelMapper.findGoodsInfoByMerchantIdAndMachineId(inno72Merchant.getId(),inno72Machine.getId());
		LOGGER.info("findGoodsInfoByMerchantIdAndMachineId is {} ", JsonUtil.toJson(list));
		sessionVo.setGoodsList(goodslist);

        //插入gameLife表
        Inno72Locale inno72Locale = inno72LocaleMapper.selectByPrimaryKey(inno72Machine.getLocaleId());
        Inno72GameUserLife life = new Inno72GameUserLife(gameUserChannel == null ? null : gameUserChannel.getGameUserId(),
                gameUserChannel == null ? null : gameUserChannel.getId(), inno72Machine.getMachineCode(),
                gameUserChannel == null ? null : gameUserChannel.getUserNick(), interact.getId(),
                interact.getName(), interact.getId(), inno72Game.getId(), inno72Game.getName(),
                inno72Machine.getLocaleId(), inno72Locale == null ? "" : inno72Locale.getMall(), null, "", null, null,
                openId, sessionVo.getSellerId() == null ? inno72Merchant.getMerchantCode() : sessionVo.getSellerId(), sessionVo.getGoodsCode() == null ? "" : sessionVo.getGoodsCode());
        LOGGER.info("插入用户游戏记录 ===> {}", JSON.toJSONString(life));
        inno72GameUserLifeMapper.insert(life);

        gameSessionRedisUtil.setSessionEx(sessionUuid, JSON.toJSONString(sessionVo));

        CommonBean.logger(
                CommonBean.POINT_TYPE_LOGIN,
                inno72Machine.getMachineCode(),
                "用户" + nickname + "，登录机器 ["+inno72Machine.getMachineCode()+"], 当前活动 ["+ interact.getName() +"]",
                interact.getId()+"|"+openId);
        //设置已经登陆
        inno72AuthInfoService.setLogged(sessionUuid);
        return Results.success();
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
        map.put("channelCode",Inno72Channel.CHANNELCODE_WECHAT);
        map.put("needPay", false);
        map.put("inno72OrderId", inno72OrderId);
        return Results.success(map);
    }

    private void parseAuthInfo(String authInfo) {
        String openId = FastJsonUtils.getString(authInfo, "openId");
        String nickname = FastJsonUtils.getString(authInfo, "nickname");
        String sexDesc = FastJsonUtils.getString(authInfo, "sexDesc");
        String sex = FastJsonUtils.getString(authInfo, "sex");
        String language = FastJsonUtils.getString(authInfo, "language");
        String city = FastJsonUtils.getString(authInfo, "city");
        String province = FastJsonUtils.getString(authInfo, "province");
        String country = FastJsonUtils.getString(authInfo, "country");
        String headImgUrl = FastJsonUtils.getString(authInfo, "headImgUrl");
    }

    private String buildParam(String machineCode,String activityId,String sessionUuid){
        StringBuilder sb = new StringBuilder("&machineCode=");
        sb.append(machineCode);
        sb.append("&activityId=");
        sb.append(activityId);
        sb.append("&sessionUuid=");
        sb.append(sessionUuid);
        sb.append("&channelType=");
        sb.append(StandardLoginTypeEnum.WEIXIN.getValue());
        LOGGER.info("微信机器二维码内容为:{}",sb.toString());
        return sb.toString();
    }

    @Override
    public boolean getCanOrder(UserSessionVo sessionVo) {
        Inno72Interact interact = inno72InteractService.findById(sessionVo.getInno72MachineVo().getActivityId());
        Inno72GameUserChannel gameUserChannel = new Inno72GameUserChannel();
        gameUserChannel.setGameUserId(sessionVo.getGameUserId());
        gameUserChannel =inno72GameUserChannelMapper.selectOne(gameUserChannel);
        return inno72AuthInfoService.findCanOrder(interact,sessionVo,gameUserChannel.getGameUserId());
    }
}
