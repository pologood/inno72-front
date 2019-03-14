package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.inno72.common.*;
import com.inno72.common.util.AesUtils;
import com.inno72.common.util.DateUtil;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.*;
import com.inno72.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service("ALI")
@Transactional
public class Inno72ALiChannelServiceImpl implements Inno72ChannelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72ALiChannelServiceImpl.class);
    @Resource
    private Inno72GameServiceProperties inno72GameServiceProperties;

    @Resource
    private Inno72GameApiService inno72GameApiService;

    @Resource
    private Inno72MachineMapper inno72MachineMapper;

    @Resource
    private Inno72OrderGoodsMapper inno72OrderGoodsMapper;

    @Resource
    private Inno72OrderHistoryMapper inno72OrderHistoryMapper;

    @Resource
    private Inno72GameMapper inno72GameMapper;

    @Resource
    private Inno72OrderMapper inno72OrderMapper;

    @Resource
    private GameSessionRedisUtil gameSessionRedisUtil;

    @Resource
    private Inno72MerchantMapper inno72MerchantMapper;

    @Resource
    private Inno72GoodsMapper inno72GoodsMapper;

    @Resource
    private Inno72InteractService inno72InteractService;

    @Resource
    private Inno72TopService inno72TopService;

    @Resource
    private Inno72ChannelMapper inno72ChannelMapper;

    @Resource
    private Inno72GameUserChannelMapper inno72GameUserChannelMapper;

    @Resource
    private Inno72GameUserMapper inno72GameUserMapper;

    @Resource
    private Inno72InteractGoodsService inno72InteractGoodsService;

    @Resource
    private IRedisUtil redisUtil;

    @Resource
    private Inno72LocaleMapper inno72LocaleMapper;

    @Resource
    private Inno72MachineService inno72MachineService;

    @Resource
    private Inno72CouponMapper inno72CouponMapper;

    @Resource
    private Inno72GameService inno72GameService;

    @Resource
    private Inno72GameUserLifeMapper inno72GameUserLifeMapper;

    @Resource
    private Inno72AuthInfoService inno72AuthInfoService;

    @Resource
    private PointService pointService;

    @Resource
    private Inno72NewretailService inno72NewretailService;

    @Value("${env}")
    private String env;

    @Resource
    private Inno72OrderAlipayMapper inno72OrderAlipayMapper;

    // todo gxg 使用枚举
    private static final String QRSTATUS_NORMAL = "0"; // 二维码正常
    private static final String QRSTATUS_INVALID = "-1"; // 二维码失效
    private static final String QRSTATUS_EXIST_USER = "-2"; // 存在用户登录

    @Override
    public String buildQrContent(Inno72Machine inno72Machine,String sessionUuid,StandardPrepareLoginReqVo req) {
        // 生成二维码流程
        String redirect = inno72GameServiceProperties.get("loginRedirect");
        String url = buildUrl(inno72Machine, redirect,sessionUuid);
        return url;
    }

    @Override
    public Result<Object> paiYangProcessBeforeLogged(String sessionUuid, UserSessionVo sessionVo, String authInfo,String traceId) {
        String accessToken = FastJsonUtils.getString(authInfo, "access_token");
        String userId = FastJsonUtils.getString(authInfo, "taobao_user_nick");
        if (StringUtil.isEmpty(accessToken)) {
            return Results.failure("accessToken 参数缺失！");
        }
        String jstUrl = inno72GameServiceProperties.get("jstUrl");
        if (StringUtil.isEmpty(jstUrl)) {
            return Results.failure("配置中心无聚石塔配置路径!");
        }

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

        // 检查二维码是否可以重复扫
        String qrStatus = this.checkQrCode(sessionUuid);
        Inno72Merchant inno72Merchant = null;
        if(sessionVo.getGoodsType()!=null && UserSessionVo.GOODSTYPE_COUPON.compareTo(sessionVo.getGoodsType()) == 0){
            inno72Merchant = inno72MerchantMapper.findByCoupon(sessionVo.getGoodsId());
        }else{
            if(!StringUtils.isEmpty(sessionVo.getGoodsId())){
                Inno72Goods goods = inno72GoodsMapper.selectByPrimaryKey(sessionVo.getGoodsId());
                String sellerId = goods.getSellerId();
                inno72Merchant = inno72MerchantMapper.selectByPrimaryKey(sellerId);
            }
        }
        String merchentCode = sessionVo.getSellerId();
        if(inno72Merchant == null && !StringUtils.isEmpty(merchentCode)){
            inno72Merchant= inno72MerchantMapper.findByMerchantCode(merchentCode);
        }
        Inno72Interact interact = inno72InteractService.findById(sessionVo.getInno72MachineVo().getActivityId());
        playCode = interact.getPlanCode();
        LOGGER.info("sessionRedirect layCode is {}", playCode);

        String nickName = inno72TopService.getMaskUserNick(sessionUuid, accessToken, inno72Merchant.getMerchantCode(), userId);

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
            userChannel = new Inno72GameUserChannel(nickName, "", channelId, inno72GameUser.getId(),
                    inno72Channel.getChannelName(), userId, accessToken,StandardLoginTypeEnum.ALIBABA.getValue());
            inno72GameUserChannelMapper.insert(userChannel);
            LOGGER.info("插入游戏用户渠道表 完成 ===> {}", JSON.toJSONString(userChannel));
        } else {
            userChannel.setAccessToken(accessToken);
            inno72GameUserChannelMapper.updateByPrimaryKey(userChannel);
        }

//		UserSessionVo sessionVo = new UserSessionVo(mid, nickName, userId, accessToken, gameId, sessionUuid,
//				inno72ActivityPlan.getId());

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

        sessionVo.setUserNick(nickName);
        sessionVo.setUserId(userId);
        sessionVo.setGameUserId(userChannel.getGameUserId());
        sessionVo.setAccessToken(accessToken);
        sessionVo.setGameId(gameId);
        sessionVo.setSessionUuid(sessionUuid);
        sessionVo.setActivityPlanId(interact.getId());
        boolean canOrder = inno72AuthInfoService.findCanOrder(interact,sessionVo,userChannel.getGameUserId());
        sessionVo.setCanOrder(canOrder);
        if(sessionVo.getGoodsType()!=null && UserSessionVo.GOODSTYPE_COUPON.compareTo(sessionVo.getGoodsType())==0){
            sessionVo.setCountGoods(true);
        }else{
            Integer goodsCount = inno72MachineService.getMachineGoodsCount(sessionVo.getGoodsId(),inno72Machine.getId());
            sessionVo.setCountGoods(goodsCount>0);
        }
        sessionVo.setChannelId(channelId);
        sessionVo.setActivityId(interact.getId());

        LOGGER.info("playCode is" + playCode);

        Integer activityType =  Inno72MachineVo.ACTIVITYTYPE_PAIYANG;

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("machineCode", inno72Machine.getMachineCode());
        resultMap.put("playCode", playCode);
        resultMap.put("qrStatus", qrStatus);
        resultMap.put("sellerId", sessionVo.getSellerId());
        resultMap.put("traceId", traceId);
        //是否走新零售入会新零售入会
        resultMap.put("paiyangType",interact.getPaiyangType());
        resultMap.put("sellSessionKey",inno72Merchant.getSellerSessionKey());
        this.dealIsVip(resultMap, sessionVo);
//		this.dealFollowSessionKey(resultMap, sessionVo);

        resultMap.put("activityType", activityType);
        resultMap.put("goodsCode", sessionVo.getGoodsCode() != null ? sessionVo.getGoodsCode() : "");

        LOGGER.info("processBeforeLogged返回聚石塔结果 is {}", resultMap);

        CommonBean.logger(
                CommonBean.POINT_TYPE_LOGIN,
                inno72Machine.getMachineCode(),
                "用户" + nickName + "，登录机器 ["+inno72Machine.getMachineCode()+"], 当前活动 ["+ interact.getName() +"]",
                interact.getId()+"|"+userId);

        return Results.success(resultMap);
    }

    @Override
    public Result<Object> order(UserSessionVo userSessionVo, String itemId ,String inno72OrderId ) {
        String accessToken = userSessionVo.getAccessToken();
        String activityId = userSessionVo.getActivityId();
        String machineCode = userSessionVo.getMachineCode();
        String sessionUuid = userSessionVo.getSessionUuid();
        // todo gxg 非登录下单不需要调用聚石塔接口
        int loginType = userSessionVo.getLoginType();
        String respJson = "";
        boolean needPay = false;
        String payQrcodeImage = "";
        String ref_order_id = "";

        if (loginType == 0) {
            try {
                // todo gxg 抽象到单独的聚石塔接口 order
                Map<String, String> requestForm = new HashMap<>();
                requestForm.put("accessToken", accessToken);
                requestForm.put("activityId", activityId);
                requestForm.put("mid", machineCode); // 实际为code
                requestForm.put("goodsId", itemId);
                requestForm.put("mixNick", userSessionVo.getUserId()); // 实际为taobao_user_nick

                LOGGER.info("调用聚石塔下单接口 参数 ======》 {}", JSON.toJSONString(requestForm));
                respJson = HttpClient.form(CommonBean.TopUrl.ORDER, requestForm, null);
            } catch (Exception e) {
                LOGGER.error("调用聚石塔失败 ! {}", e.getMessage(), e);
                return Results.failure("聚石塔调用失败!");
            }

            if (StringUtil.isEmpty(respJson)) {
                return Results.failure("聚石塔无返回数据!");
            }

            LOGGER.info("调用聚石塔接口  【下单】返回 ===> {}", respJson);

            String msgCode = FastJsonUtils.getString(respJson, "msg_code");
            if (msgCode.equals("SUCCESS")) {
                String actualFee = FastJsonUtils.getString(respJson, "actual_fee");
                if (StringUtil.isNotEmpty(actualFee) && actualFee.equals("0")) {
                    needPay = false;
                }

                String _payQrcodeImage = FastJsonUtils.getString(respJson, "pay_qrcode_image");
                if (StringUtil.isNotEmpty(_payQrcodeImage)) {
                    needPay = true;
                    payQrcodeImage = _payQrcodeImage;
//					payQrcodeImage = this.getPayQrUrl(_payQrcodeImage ,sessionUuid, userSessionVo);
//					userSessionVo.setScanPayUrl(payQrcodeImage);
                    Inno72OrderAlipay inno72OrderAlipay = new Inno72OrderAlipay();
                    inno72OrderAlipay.setOrderId(userSessionVo.getInno72OrderId());
                    inno72OrderAlipay.setMachineCode(userSessionVo.getMachineCode());
                    inno72OrderAlipay.setCreateTime(new Date());
                    inno72OrderAlipay.setActivityId(userSessionVo.getActivityId());
                    inno72OrderAlipay.setStatus(Inno72OrderAlipay.STATUS_ENUM.CREATE.getKey());
                    inno72OrderAlipayMapper.insert(inno72OrderAlipay);

                    //开始获取订单支付状态
					GetAliPayStatusThread.run(inno72OrderAlipay);
                }

            } else {
                return Results.failure("淘宝下单失败！");
            }

            ref_order_id = FastJsonUtils.getString(respJson, "order_id");
        }

        userSessionVo.setRefOrderId(ref_order_id);
        userSessionVo.setNeedPay(needPay);

//		gameSessionRedisUtil.setSession(sessionUuid, JSON.toJSONString(userSessionVo));
        // 更新第三方订单号进inno72 order
        Result<String> stringResult = inno72GameService
                .updateRefOrderId(inno72OrderId, ref_order_id, userSessionVo.getUserId());
        LOGGER.info("修改第三方订单进inno72——order 结果 {}", JSON.toJSONString(stringResult));

        // 如果有支付链接则返回支付链接
        Map<String, Object> map = new HashMap<>();
        map.put("channelCode",Inno72Channel.CHANNELCODE_ALI);
        map.put("needPay", needPay);
        map.put("payQrcodeImage", payQrcodeImage);
        map.put("inno72OrderId", inno72OrderId);
        return Results.success(map);
    }

    @Override
    public void feedBackInTime(String inno72OrderId, String machineCode) {
        //淘宝回流业务
        inno72NewretailService.feedBackInTime(inno72OrderId,machineCode);
    }

    /**
     * 检查二维码是否可以重复扫
     * @param sessionUuid
     * @return
     */
    private synchronized String checkQrCode(String sessionUuid) {
        // 判断是否有他人登录以及二维码是否过期
        String qrStatus = QRSTATUS_NORMAL;
        LOGGER.info("sessionUuid is {}", sessionUuid);
        return qrStatus;
    }
    /**
     *
     * @return
     */
    private String buildUrl(Inno72Machine inno72Machine, String redirect,String sessionUuid){
        String bluetoothAdd = "";
        String bluetoothAddAes = "";
        if (inno72Machine != null) {
            bluetoothAdd = inno72Machine.getBluetoothAddress();
            if (!StringUtil.isEmpty(bluetoothAdd)) {
                bluetoothAddAes = AesUtils.encrypt(bluetoothAdd);
            }
        }

        LOGGER.info("Mac蓝牙地址 {} ", bluetoothAddAes);

        String url = String.format(
                "%s/?sessionUuid=%s&env=%s&bluetoothAddAes=%s&machineCode=%s",
                redirect, sessionUuid, env, bluetoothAddAes, inno72Machine.getMachineCode());


        return url;
    }

    /**
     * 获取活动商品展示个数
     * @param gameId
     * @return
     */
    private Integer findIntentGoodsSize(String gameId) {
        //查找对应的游戏里面的显示条数
        Inno72Game game = inno72GameService.findById(gameId);
        return game.getMaxGoodsNum();
    }

    /**
     * 处理是否入会
     */
    void dealIsVip(Map<String, Object> resultMap, UserSessionVo sessionVo) {
        LOGGER.info("dealIsVip params sessionVo is {}", sessionVo);
        // 如果需要入会写入会信息
        String isVip = sessionVo.getIsVip();
        if (StringUtil.isNotEmpty(isVip) && sessionVo.getIsVip().equals("1")) {
            String goodsId = sessionVo.getGoodsId();
            if (!StringUtil.isEmpty(goodsId)) {
                if(sessionVo.getGoodsType()!=null && UserSessionVo.GOODSTYPE_COUPON.compareTo(sessionVo.getGoodsType()) == 0){
                    Inno72Coupon inno72Coupon = inno72CouponMapper.selectByPrimaryKey(goodsId);
                    sessionVo.setGoodsCode(inno72Coupon.getCode());
                }else{
                    Inno72Goods inno72Goods = inno72GoodsMapper.selectByPrimaryKey(goodsId);
                    String goodsCode = inno72Goods.getCode();
                    sessionVo.setGoodsCode(goodsCode);
                }
            }
            String goodsCode = sessionVo.getGoodsCode();
            LOGGER.info("返回给聚石塔的入会信息 goodsCode is {}  isVip is {}, sessionKey is {}", goodsCode, sessionVo.getIsVip(), sessionVo.getSessionKey());
            resultMap.put("goodsCode", goodsCode);
            resultMap.put("isVip", sessionVo.getIsVip());
            resultMap.put("sessionKey", sessionVo.getSessionKey());
        }
    }

    @Override
    public Result<Object> orderPolling(UserSessionVo userSessionVo, MachineApiVo vo) {
        LOGGER.info("orderPolling userSessionVo {}", userSessionVo);

        if (userSessionVo == null) {
            return Results.failure("登录失效!");
        }

        String orderId = userSessionVo.getRefOrderId();
        if (StringUtil.isEmpty(orderId)) {
            LOGGER.info("第三方订单号不存在!");
            return Results.failure("请求失败");
        }

//		Result orderPollingResult = inno72TopService.orderPolling(sessionUuid, orderId);
//		if (orderPollingResult.getCode() == Result.FAILURE) {
//			return Results.failure(orderPollingResult.getMsg());
//		}

        // todo gxg 统一维护到聚石塔服务 order-polling
        String accessToken = userSessionVo.getAccessToken();
        Map<String, String> requestForm = new HashMap<>();

        requestForm.put("accessToken", accessToken);
        requestForm.put("orderId", orderId);

        LOGGER.info("调用聚石塔接口【下单支付状态】 orderId {}, machineCode {} , 参数 {}", orderId, userSessionVo.getMachineCode(), JSON.toJSONString(requestForm));

        String respJson = HttpClient.form(CommonBean.TopUrl.ORDER_POLLING, requestForm, null);
        if (StringUtil.isEmpty(respJson)) {
            return Results.failure("聚石塔无返回数据!");
        }

        LOGGER.info("调用聚石塔接口【下单支付状态】 orderId {}, machineCode {} , 返回 {}", orderId, userSessionVo.getMachineCode(), JSON.toJSONString(respJson));

        try {
            String msg_code = FastJsonUtils.getString(respJson, "msg_code");

            if (!msg_code.equals("SUCCESS")) {
                String msg_info = FastJsonUtils.getString(respJson, "msg_info");
                return Results.failure(msg_info);
            }

            boolean model = Boolean.valueOf(FastJsonUtils.getString(respJson, "model"));

            if (model) {
                Inno72Order inno72Order = inno72OrderMapper.selectByRefOrderId(orderId);
                if (inno72Order != null) {
                    inno72Order.setPayStatus(Inno72Order.INNO72ORDER_PAYSTATUS.SUCC.getKey());
                    inno72Order.setPayTime(LocalDateTime.now());
                    inno72OrderMapper.updateByPrimaryKeySelective(inno72Order);
                    inno72OrderHistoryMapper.insert(new Inno72OrderHistory(inno72Order.getId(),
                            inno72Order.getOrderNum(), JSON.toJSONString(inno72Order), "修改状态为已支付"));

                    Map<String, Object> goodsParams = new HashMap<>();
                    goodsParams.put("goodsType", Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT.getKey());
                    goodsParams.put("orderId", inno72Order.getId());
                    // todo gxg 处理报错情况
                    Inno72OrderGoods goods = inno72OrderGoodsMapper.selectByOrderIdAndGoodsType(goodsParams);
                    goods.setStatus(Inno72Order.INNO72ORDER_PAYSTATUS.SUCC.getKey());
                    inno72OrderGoodsMapper.updateByPrimaryKeySelective(goods);

                    userSessionVo.setRefOrderStatus(inno72Order.getRefOrderStatus());
                    pointService.innerPoint(JSON.toJSONString(userSessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.PAY);
                    this.taoBaoDataSyn(vo.getSessionUuid(), JSON.toJSONString(requestForm), respJson, Inno72TaoBaoCheckDataVo.ENUM_INNO72_TAOBAO_CHECK_DATA_VO_TYPE.ORDER);
                }
            }

            Map<String, Object> result = new HashMap<>();

            // 如果需要支付 polling时 需要返回货道信息
            boolean needPay = userSessionVo.getNeedPay();
            if (needPay) {
                String goodsId = userSessionVo.getGoodsId();
                List<String> goodsIds = new ArrayList<>();
                goodsIds.add(goodsId);
                inno72GameApiService.setChannelInfo(userSessionVo, result, goodsIds);
            }
            result.put("model", model);
            LOGGER.info("orderPolling 返回结果 orderId {}, machineCode {} , result {}", orderId, userSessionVo.getMachineCode(), JSON.toJSONString(result));
            return Results.success(result);

        } catch (Exception e) {
            LOGGER.info("解析聚石塔返回数据异常! ===>  {}", e.getMessage(), e);
            return Results.failure("解析聚石塔返回数据异常!");
        }
    }

    private void taoBaoDataSyn(String sessionUuid, String reqBody, String resBody, Inno72TaoBaoCheckDataVo.ENUM_INNO72_TAOBAO_CHECK_DATA_VO_TYPE type) {
        Inno72TaoBaoCheckDataVo inno72TaoBaoCheckDataVo = new Inno72TaoBaoCheckDataVo();
        inno72TaoBaoCheckDataVo.setSessionUuid(sessionUuid);
        inno72TaoBaoCheckDataVo.setReqBody(reqBody);
        inno72TaoBaoCheckDataVo.setRspBody(resBody);
        inno72TaoBaoCheckDataVo.setType(type.getType());
        pointService.innerTaoBaoDataSyn(inno72TaoBaoCheckDataVo);
    }

    @Override
    public void shipment(String channelId, String machineCode, UserSessionVo userSessionVo,String orderId,MachineApiVo vo) {
        if (StringUtil.isNotEmpty(orderId)) {
            new Thread(new DeliveryRecord(channelId, machineCode, userSessionVo)).run();
        } else {
            LOGGER.info("调用出货无orderId 请求参数=>{}", JSON.toJSONString(vo));
        }
    }
    class DeliveryRecord implements Runnable {

        private String channelId;
        private UserSessionVo userSessionVo;
        private String machineCode;

        public DeliveryRecord(String channelId, String machineCode, UserSessionVo userSessionVo) {
            this.channelId = channelId;
            this.machineCode = machineCode;
            this.userSessionVo = userSessionVo;
        }

        @Override
        public void run() {
            // todo gxg 抽到聚石塔 出货
            Map<String, String> requestForm = new HashMap<>();

            requestForm.put("accessToken", userSessionVo.getAccessToken());
            requestForm.put("orderId", userSessionVo.getRefOrderId()); // 安全ua
            requestForm.put("mid", machineCode);// umid 实际为code
            requestForm.put("channelId", channelId);// 互动实例ID

            String respJson = "";
            try {
                respJson = HttpClient
                        .form(CommonBean.TopUrl.DELIVERY_RECORD, requestForm, null);
            } catch (Exception e) {
                LOGGER.info("");
            }
            if (StringUtil.isEmpty(respJson)) {
                LOGGER.info("聚石塔无返回数据!");
            }

            LOGGER.info("调用聚石塔接口  【通知出货】返回 ===> {}", JSON.toJSONString(respJson));

            String msg_code = FastJsonUtils.getString(respJson, "msg_code");
            if (!msg_code.equals("SUCCESS")) {
                String msg_info = FastJsonUtils.getString(respJson, "msg_info");
                LOGGER.info("返回非成功状态，不能更细订单状态为成功 {}", respJson);
            }

        }
    }
}
