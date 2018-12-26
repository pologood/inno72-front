package com.inno72.service.impl;

import com.inno72.common.Inno72BizException;
import com.inno72.common.RedisConstants;
import com.inno72.common.Result;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72ChannelMapper;
import com.inno72.mapper.Inno72GameUserLifeMapper;
import com.inno72.mapper.Inno72GameUserLoginMapper;
import com.inno72.mapper.Inno72OrderMapper;
import com.inno72.model.*;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.*;
import com.inno72.vo.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class Inno72UnStandardServiceImpl implements Inno72UnStandardService {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MsgUtil msgUtil;

    @Autowired
    private Inno72OrderRefundService inno72OrderRefundService;

    @Autowired
    private IRedisUtil iRedisUtil;

    @Autowired
    private Inno72ChannelMapper inno72ChannelMapper;

    @Autowired
    private Inno72GameUserChannelService inno72GameUserChannelService;

    @Autowired
    private Inno72AuthInfoService inno72AuthInfoService;
    @Autowired
    private Inno72GameUserLifeMapper inno72GameUserLifeMapper;

    @Autowired
    private Inno72OrderService inno72OrderService;

    @Autowired
    private Inno72OrderMapper inno72OrderMapper;

    @Autowired
    private Inno72GameUserLoginMapper inno72GameUserLoginMapper;

    @Autowired
    private Inno72PayService inno72PayService;

    @Value("${phoneverificationcode_limit_time}")
    private Integer phoneverificationcodeLimitTime;

    @Value("${duduji.appid}")
    private String dudujiAppId;

    private final String SYMBOLS = "0123456789"; // 数字

    private final Random RANDOM = new SecureRandom();

    @Override
    public void getPhoneVerificationCode(String sessionUuid, String phone,Integer type) {
        LOGGER.info("getPhoneVerificationCode sessionUuid = {}, phone = {}, type ={}",sessionUuid,phone,type);
        String code = getNonceStr();
        LOGGER.info("getPhoneVerificationCode phone = {},code={}",phone,code);
        //发送短信
        sendSms(phone,code);
        //将code放入redis
        String key = RedisConstants.PHONEVERIFICATIONCODE_REDIS_KEY + phone;
        iRedisUtil.setex(key,5*60,code);
        //记录redis时间
        key = RedisConstants.PHONEVERIFICATIONCODE_TIME_LIMIT_REDIS_KEY+phone;
        iRedisUtil.setex(key,60,"1");
        //记录redis次数
        if(type == null){
            UserSessionVo sessionVo = new UserSessionVo(sessionUuid);
            String activityId = sessionVo.getActivityId();
            key = RedisConstants.PHONEVERIFICATIONCODE_TIMES_LIMIT_REDIS_KEY+activityId +":"+phone;
        }else{
            //不根据活动限制
            key = RedisConstants.PHONEVERIFICATIONCODE_TIMES_LIMIT_REDIS_KEY+phone;
        }

        if(iRedisUtil.exists(key)){
            iRedisUtil.incrBy(key,1);
        }else{
            iRedisUtil.incrBy(key,1);
            iRedisUtil.expire(key,phoneverificationcodeLimitTime*60);
        }
    }

    @Override
    public String checkPhoneVerificationCode(String sessionUuid, String phone, String verificationCode,Integer operatingSystem,String phoneModel,String scanSoftware,String clientInfo,Integer type,String openId) {
        LOGGER.info("checkPhoneVerificationCode sessionUuid = {}, phone = {}, verificationCode ={} ",sessionUuid,phone,verificationCode);
        String key = RedisConstants.PHONEVERIFICATIONCODE_REDIS_KEY+phone;
        String code = iRedisUtil.get(key);
        if(StringUtils.isEmpty(code)){
            throw new Inno72BizException("验证码过期");
        }
        if(code.equals(verificationCode)){
            if(type == null){
                //登陆
                String traceId = StringUtil.getUUID();
                Inno72AuthInfo ai = new Inno72AuthInfo();
                ai.setPhone(phone);
                ai.setChannelType(StandardLoginTypeEnum.INNO72.getValue()+"");
                ai.setOperatingSystem(operatingSystem);
                ai.setPhoneModel(phoneModel);
                ai.setScanSoftware(scanSoftware);
                ai.setClientInfo(clientInfo);
                String authInfo = JsonUtil.toJson(ai);
                Result result = inno72AuthInfoService.processBeforeLogged(sessionUuid, authInfo, traceId);
                if(result.getCode() != Result.SUCCESS){
                    throw new Inno72BizException(result.getMsg());
                }
                boolean success = inno72AuthInfoService.setLogged(sessionUuid);
                if(!success){
                    throw new Inno72BizException("设置登陆异常");
                }
            }else{
                //关联微信用户和手机号用户
                String gameUserId = inno72GameUserChannelService.joinUser(openId,phone);
                return gameUserId;
            }
        }else{
            throw new Inno72BizException("验证码错误");
        }
        return null;
    }

    @Override
    public String changePayType(String sessionUuid, Integer payType) {
        //修改订单支付方式
        Inno72Order order = inno72OrderService.changePayType(sessionUuid,payType);
        return inno72PayService.pay(order);
    }

    @Override
    public void updatePhoto(String sessionUuid, String photoImg) {
        UserSessionVo userSessionVo = new UserSessionVo(sessionUuid);
        String gameUserLoginId = userSessionVo.getGameUserLoginId();
        Inno72GameUserLogin login = new Inno72GameUserLogin();
        login.setId(gameUserLoginId);
        login.setUrl(photoImg);
        inno72GameUserLoginMapper.updateByPrimaryKeySelective(login);
    }

    @Override
    public void payCallback(String retCode, String billId, String buyerId, String extra, String fee, String outTradeNo, String spId, Integer terminalType, Integer type) {
        LOGGER.info("payCallback retCode ={},billId={},buyerId={},extra={},fee={},outTradeNo={},spId={},terminalType={},type={}",retCode,billId,buyerId,extra,fee,outTradeNo,spId,terminalType,type);
        //更新订单状态为一支付
        if(Result.SUCCESS == Integer.parseInt(retCode)){
            inno72OrderService.updateOrderStatusAndPayStatus(outTradeNo,Inno72Order.INNO72ORDER_ORDERSTATUS.PAY.getKey(),Inno72Order.INNO72ORDER_PAYSTATUS.SUCC.getKey());
        }
    }

    @Override
    public void gamePointTime(String sessionUuid, Integer type) {
        UserSessionVo userSessionVo = new UserSessionVo(sessionUuid);
        Inno72GameUserLife userLife = inno72GameUserLifeMapper.selectByUserChannelIdLast(userSessionVo.getUserId());
        if(type == Inno72GameUserLife.GAME_START_TIME_TYPE){
            userLife.setGameStartTime(LocalDateTime.now());
        }else if(type == Inno72GameUserLife.GAME_END_TIME_TYPE){
            userLife.setGameEndTime(LocalDateTime.now());
        }else if(type == Inno72GameUserLife.SHARE_TIME_TYPE){
            userLife.setShareTime(LocalDateTime.now());
        }else if(type == Inno72GameUserLife.SHIPMENT_TIME_TYPE){
            userLife.setShipmentTime(LocalDateTime.now());
        }
        inno72GameUserLifeMapper.updateByPrimaryKeySelective(userLife);

    }

    @Override
    public String joinPhoneFlag(WxMpUser user) {

        if(user == null) return null;
        user.setAppId(dudujiAppId);
        Inno72Channel channel = inno72ChannelMapper.findByCode(Inno72Channel.CHANNELCODE_WECHAT);
        Inno72GameUserChannel userChannel = inno72GameUserChannelService.findInno72GameUserChannel(channel.getId(),user.getOpenId(),dudujiAppId);
        if(userChannel == null){
            //插入微信用户
            inno72GameUserChannelService.saveWechatUser(user);
            return null;
        }else{
            inno72GameUserChannelService.updateWechatUser(user,userChannel.getId());
        }
        channel = inno72ChannelMapper.findByCode(Inno72Channel.CHANNELCODE_INNO72);
        userChannel = inno72GameUserChannelService.findByGameUserIdAndChannelId(userChannel.getGameUserId(),channel.getId());
        if(userChannel == null){
            return null;
        }
        return userChannel.getGameUserId();
    }

    @Override
    public List<OrderVo> orderList(String gameUserId,Integer pageNum,Integer pageSize) {
        return inno72OrderService.orderList(gameUserId,pageNum,pageSize);
    }
    @Override
    public void refundAsk(String gameUserId) {
        //查找未掉货的订单
        List<Inno72Order> list = inno72OrderService.findUnShipmentOrder(gameUserId);
        if(list.size()>0){
            List<Inno72OrderRefund> saveRefundList = new ArrayList<Inno72OrderRefund>();
            for(Inno72Order order : list){
                List<Inno72OrderRefund> refundList = inno72OrderRefundService.findByOrderId(order.getId());
                if(refundList.size() == 0){
                    //Inno72OrderRefund
                    Inno72OrderRefund inno72OrderRefund = new Inno72OrderRefund();
                    inno72OrderRefund.setAmount(order.getOrderPrice());
                    inno72OrderRefund.setAuditStatus(Inno72OrderRefund.REFUND_AUDITSTATUS.UNAUDIT.getKey());
                    inno72OrderRefund.setCreateTime(new Date());
                    inno72OrderRefund.setOrderId(order.getId());
                    Inno72Channel channel = inno72ChannelMapper.findByCode(Inno72Channel.CHANNELCODE_INNO72);
                    Inno72GameUserChannel user = inno72GameUserChannelService.findByGameUserIdAndChannelId(order.getUserId(),channel.getId());
                    if(user!=null){
                        inno72OrderRefund.setPhone(user.getPhone());
                    }
                    inno72OrderRefund.setStatus(Inno72OrderRefund.REFUND_STATUS.NEW.getKey());
                    inno72OrderRefund.setUpdateTime(inno72OrderRefund.getCreateTime());
                    inno72OrderRefund.setUserId(order.getUserId());
                    saveRefundList.add(inno72OrderRefund);
                }
            }
            if(saveRefundList.size()>0){
                for(Inno72OrderRefund refund:saveRefundList){
                    inno72OrderRefundService.save(refund);
                }
            }
        }else{
            LOGGER.info("无支付未掉货订单，gameUserId={}",gameUserId);
            throw new Inno72BizException("无支付未掉货订单");
        }
    }

    /**
     * 发送短信验证码
     * @param phone
     * @param code
     */
    private void sendSms(String phone, String code) {
        //yp_validate_code
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("code",code);
        msgUtil.sendSMS("interact_validate_code",params,phone,"inno72-game-service");
    }

    /**
     * 获取长度为 4 的随机数字
     * @return 随机数字
     */
    private String getNonceStr() {

        char[] nonceChars = new char[4];

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }

        return new String(nonceChars);
    }

}
