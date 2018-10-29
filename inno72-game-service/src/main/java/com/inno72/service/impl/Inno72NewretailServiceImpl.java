package com.inno72.service.impl;

import com.inno72.common.Inno72BizException;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.mapper.Inno72FeedBackLogMapper;
import com.inno72.mapper.Inno72GoodsMapper;
import com.inno72.mapper.Inno72MerchantMapper;
import com.inno72.model.Inno72FeedBackLog;
import com.inno72.model.Inno72Goods;
import com.inno72.model.Inno72MachineDevice;
import com.inno72.model.Inno72Merchant;
import com.inno72.service.Inno72MachineDeviceService;
import com.inno72.service.Inno72NewretailService;
import com.inno72.vo.DeviceParamVo;
import com.inno72.vo.DeviceVo;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class Inno72NewretailServiceImpl implements Inno72NewretailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72NewretailServiceImpl.class);

    @Autowired
    private Inno72MachineDeviceService inno72MachineDeviceService;

    @Autowired
    private Inno72GoodsMapper inno72GoodsMapper;

    @Autowired
    private Inno72MerchantMapper inno72MerchantMapper;

    @Value("${sell_session_key}")
    private String sellSessionKey;

    @Autowired
    Inno72FeedBackLogMapper inno72FeedBackLogMapper;

    @Autowired
    private TaobaoClient client;
//    public static void main(String[] args) throws Exception {
//        Long storeId = findStores(sessionKey,"吉羏测试");
//        System.out.println(storeId);
//        String deviceCode = new Inno72NewretailServiceImpl().saveDevice(sessionKey,"设备3",187209386L,"ANDROID","SAMPLE_MACHINE","device003");
//        System.out.println(deviceCode); //049AA6585C
//        findDeviceByStoreId(sessionKey,187209386L); //"device_code":"A6B820BF7E"
//        Boolean flag = getMemberIdentity(sessionKey,"aaa");
//        System.out.println(flag);
//        getStoreMemberurl(sessionKey,"A6B820BF7E");
//        deviceVendorFeedback(sessionKey,"001","tmall_trade","049AA6585C","VENDING_MACHINE_SHIPMENT","001","001","001",
//                "001","2018-10-08 10:28:29","001");
//    }
    @Override
    public Long findStores(String sessionKey, String storeName) throws Exception {
        SmartstoreStoresQueryRequest req = new SmartstoreStoresQueryRequest();
        req.setQuery(storeName);
        SmartstoreStoresQueryResponse rsp = client.execute(req, sessionKey);
        LOGGER.debug("findStores storeName={} response={}",storeName,rsp.getBody());
        List<SmartstoreStoresQueryResponse.StoreDo> list = rsp.getStores();
        long size = rsp.getTotalCount();
        if(size == 1){
            return list.get(0).getStoreId();
        }else if(size == 0){
            LOGGER.error("查找门店为空storeName={}",storeName);
            throw new Inno72BizException("查找门店为空");
        }else{
            LOGGER.error("返回多条门店信息storeName={}",storeName);
            throw new Inno72BizException("返回多条门店信息");
        }
    }

    /**
     * 新增机器
     * @param sessionKey
     * @param deviceName 设备名称
     * @param storeId 门店id
     * @param osType 操作系统类型：
     *               WINDOWS("WINDOWS", "WINDOWS"),
     *               ANDROID("ANDROID", "ANDROID"),
     *               IOS("IOS", "IOS"), LINUX("LINUX", "LINUX"), OTHER("OTHER", "OTHER");
     *  deviceType 设备类型：
     *                   CAMERA("CAMERA", "客流摄像头"), SHELF("SHELF", "云货架"),
     *                   MAKEUP_MIRROR("MAKEUP_MIRROR", "试妆镜"), FITTING_MIRROR("FITTING_MIRROR", "试衣镜"),
     *                   VENDOR("VENDOR", "售货机"), WIFI("WIFI","WIFI探针"), SAMPLE_MACHINE("SAMPLE_MACHINE","派样机"),
     *                   DOLL_MACHINE("DOLL_MACHINE", "娃娃机"), INTERACTIVE_PHOTO("INTERACTIVE_PHOTO", "互动拍照"),
     *                   INTERACTIVE_GAME("INTERACTIVE_GAME", "互动游戏"), USHER_SCREEN("USHER_SCREEN", "智慧迎宾屏"),
     *                   DRESSING("DRESSING", "闪电换装"), MAGIC_MIRROR("MAGIC_MIRROR", "百搭魔镜"),
     *                   SHOES_FITTING_MIRROR("SHOES_FITTING_MIRROR", "试鞋镜"), SKIN_DETECTION("SKIN_DETECTION", "肌肤测试仪"),
     *                   FOOT_DETECTION("FOOT_DETECTION", "测脚仪"),
     *                   RFID_SENSOR("RFID_SENSOR", "RFID"),touch_machine("touch_machine","导购一体屏")
     * @param outerCode  商家自定义设备编码
     * @throws ApiException
     */
    @Override
    public String saveDevice(String sessionKey, String deviceName, Long storeId, String osType, String outerCode) throws Exception {
        SmartstoreDeviceAddRequest req = new SmartstoreDeviceAddRequest();
        req.setDeviceName(deviceName);
        req.setStoreId(storeId);
        req.setOsType(osType);
        req.setDeviceType("VENDOR");
        req.setOuterCode(outerCode);
        SmartstoreDeviceAddResponse rsp = client.execute(req, sessionKey);
        LOGGER.debug("saveDevice deviceName ={},storeId = {},osType = {},deviceType = {},outerCode = {},response = {}",deviceName,storeId,osType,"VENDOR",outerCode,rsp.getBody());
        if(!StringUtils.isEmpty(rsp.getErrorCode())){
            LOGGER.error("saveDevice error deviceName ={},storeId = {},osType = {},deviceType = {},outerCode = {},response = {}",deviceName,storeId,osType,"VENDOR",outerCode,rsp.getBody());
            throw new Inno72BizException("保存设备天猫返回错误："+rsp.getSubMsg());
        }else{
            return rsp.getDeviceCode();
        }
    }

    /**
     * 根据门店id获取devicecode
     * @param sessionKey
     * @param storeId
     * @throws ApiException
     */
    public String findDeviceByStoreId(String sessionKey,Long storeId) throws ApiException {
        SmartstoreDeviceQueryRequest req = new SmartstoreDeviceQueryRequest();
        req.setStoreId(storeId);
        req.setPageNum(1L);
        req.setPageSize(20L);
        SmartstoreDeviceQueryResponse rsp = client.execute(req, sessionKey);
        if(rsp.getDeviceInfoList()!=null&&rsp.getDeviceInfoList().size()>0){
            return rsp.getDeviceInfoList().get(0).getDeviceCode();
        }
        return null;
    }

    /**
     * 是否入会
     * mixNick:混淆的昵称
     */
    @Override
    public Boolean getMemberIdentity(String sessionKey, String mixNick) throws ApiException {
        TmallDeviceMemberIdentityGetRequest req = new TmallDeviceMemberIdentityGetRequest();
        req.setMixNick(unescape(mixNick));
        TmallDeviceMemberIdentityGetResponse rsp = client.execute(req, sessionKey);
        String identityResBody = rsp.getBody();
        LOGGER.debug("getMemberIdentity mixNick = {},response = {}",mixNick,identityResBody);
        String grade_name = FastJsonUtils.getString(identityResBody, "grade_name");
        return !StringUtils.isEmpty(grade_name);
    }

    /**
     * 获取入会二维码
     * @param sessionKey
     * @param deviceCode
     * @throws ApiException
     */
    @Override
    public String getStoreMemberurl(String sessionKey, String deviceCode,String callbackUrl) throws ApiException {
        TmallDeviceBrandMemberurlGetRequest req = new TmallDeviceBrandMemberurlGetRequest();
        req.setDeviceCode(deviceCode);
        req.setLongterm(true);
        req.setCallbackUrl(callbackUrl);
        TmallDeviceBrandMemberurlGetResponse rsp = client.execute(req, sessionKey);
        LOGGER.debug("getStoreMemberurl deviceCode = {},response = {}",deviceCode,rsp.getBody());
        return rsp.getShortImgUrl();
    }
    /**
     * 数据回流
     * @param sessionKey
     * @param tradeNo  订单编号
     * @param tradeType "trade_type枚举值： alipay_trade（支付宝订单类型，对应的trade_no设置支付宝订单号） tmall_trade（
     *                  天猫/淘宝订单类型，对应的trade_no设置天猫/淘宝订单号）"
     * @param deviceCode 硬件CODE
     * @param action ACTION枚举值： BUY_CLICK_START(广告首页、点击开始购买)
     *               ITEM_CLICK（商品点击时必须设置ITEM_ID）
     *               BUY_CLICK（点击购买/领取必须设置ITEM_ID）
     *               VENDING_MACHINE_SHIPMENT（派样机出货时TRADE_TYPE、TRADE_NO要设置）
     *               SHIP_CNT (商品掉货成功，必须设置ITEM_ID） SHARE_CLICK（点击分享）
     *               RECEIVE_COUPONS (扫码领取优惠券时必须设置COUPON_ID)
     * @param itemId 商品ID，item_id 在action为ITEM_CLICK、BUY_CLICK、VENDING_MACHINE_SHIPMENT、SHIP_CNT必须传入； 必须使用淘宝商品id，否则失败
     * @param couponId "例如官方领取优惠券链接里的activityId： https://taoquan.taobao.com/coupon/unify_apply.htm?sellerId=2649119619&activityId=9d390579777e41a981b54aa4d6154f5e"
     * @param userNick 用户昵称（混淆）
     * @param outerBizId 数据外部编码，保证数据唯一性
     * @param opTime 操作时间，后续统一使用该字段，考虑兼容，start_time跟该字段含义一致 2018-01-01 00:00:00
     * @param outerUser 硬件识别的用户标识
     * @throws ApiException
     */
    @Override
    public String deviceVendorFeedback(String sessionKey, String tradeNo, String tradeType, String deviceCode, String action
            , String itemId, String couponId, String userNick, String outerBizId, String opTime, String outerUser) throws ApiException {
        SmartstoreDeviceVendorFeedbackRequest req = new SmartstoreDeviceVendorFeedbackRequest();
        req.setTradeNo(tradeNo);
        req.setTradeType(tradeType);
        req.setDeviceCode(deviceCode);
        req.setAction(action);
        req.setItemId(itemId);
        req.setCouponId(couponId);
        req.setUserNick(userNick);
        req.setOuterBizId(outerBizId);
        req.setOpTime(StringUtils.parseDateTime(opTime));
        req.setOuterUser(outerUser);
        SmartstoreDeviceVendorFeedbackResponse rsp = client.execute(req, sessionKey);
        LOGGER.debug("deviceVendorFeedback tradeNo={},tradeType={},deviceCode={},action={}," +
                "itemId={},couponId={},userNick={},outerBizId={},opTime={},outerUser={},response={}",
                tradeNo,tradeType,deviceCode,action,itemId,couponId,userNick,outerBizId,opTime,outerUser,rsp.getBody());
        return rsp.getBody();
    }
    /**
     * 数据回流
     * @param sessionKey
     * @param tradeNo  订单编号
     *  tradeType "trade_type枚举值： alipay_trade（支付宝订单类型，对应的trade_no设置支付宝订单号） tmall_trade（
     *                  天猫/淘宝订单类型，对应的trade_no设置天猫/淘宝订单号）"
     * @param deviceCode 硬件CODE
     *  action ACTION枚举值： BUY_CLICK_START(广告首页、点击开始购买)
     *               ITEM_CLICK（商品点击时必须设置ITEM_ID）
     *               BUY_CLICK（点击购买/领取必须设置ITEM_ID）
     *               VENDING_MACHINE_SHIPMENT（派样机出货时TRADE_TYPE、TRADE_NO要设置）
     *               SHIP_CNT (商品掉货成功，必须设置ITEM_ID） SHARE_CLICK（点击分享）
     *               RECEIVE_COUPONS (扫码领取优惠券时必须设置COUPON_ID)
     * @param itemId 商品ID，item_id 在action为ITEM_CLICK、BUY_CLICK、VENDING_MACHINE_SHIPMENT、SHIP_CNT必须传入； 必须使用淘宝商品id，否则失败
     * couponId "例如官方领取优惠券链接里的activityId： https://taoquan.taobao.com/coupon/unify_apply.htm?sellerId=2649119619&activityId=9d390579777e41a981b54aa4d6154f5e"
     * userNick 用户昵称（混淆）
     * outerBizId 数据外部编码，保证数据唯一性
     * opTime 操作时间，后续统一使用该字段，考虑兼容，start_time跟该字段含义一致 2018-01-01 00:00:00
     * outerUser 硬件识别的用户标识
     * @throws ApiException
     */
    @Override
    public String deviceVendorFeedback(String sessionKey, String tradeNo, String deviceCode, String itemId, String opTime,String userNick,String merchantName,String merchantCode) throws ApiException {
        SmartstoreDeviceVendorFeedbackRequest req = new SmartstoreDeviceVendorFeedbackRequest();
        req.setTradeNo(tradeNo);
        String tradeType = "tmall_trade";
        req.setTradeType(tradeType);
        req.setDeviceCode(deviceCode);

        String action = "SHIP_CNT";
        req.setAction(action);
        req.setItemId(itemId);
        req.setOpTime(StringUtils.parseDateTime(opTime));
        req.setUserNick(userNick);
        SmartstoreDeviceVendorFeedbackResponse rsp = client.execute(req, sessionKey);
        LOGGER.debug("deviceVendorFeedback tradeNo={},tradeType={},deviceCode={},action={}," +
                        "itemId={},opTime={},response={}",
                tradeNo,tradeType,deviceCode,action,itemId,opTime,rsp.getBody());
        //插入日志
        Inno72FeedBackLog log = new Inno72FeedBackLog();
        log.setGoodsId(itemId);
        log.setOrderId(tradeNo);
        log.setMerchantName(merchantName);
        log.setMerchantCode(merchantCode);
        log.setResponseBody(rsp.getBody());
        log.setOrderTime(LocalDateTime.now());
        inno72FeedBackLogMapper.insert(log);
        return rsp.getBody();
    }

    @Override
    public void saveMachine(List<DeviceVo> list) throws Exception {
        LOGGER.debug("saveMachine param = {}",JsonUtil.toJson(list));
        if(list!= null && list.size()>0){
            for(DeviceVo deviceVo:list){
                checkDeviceParamVo(deviceVo);

                //获取sessionKey
                Inno72Goods goods = inno72GoodsMapper.selectByPrimaryKey(deviceVo.getGoodsId());
                String sellerId = goods.getSellerId();
                Inno72Merchant merchant = inno72MerchantMapper.selectByPrimaryKey(goods.getSellerId());
//                String sessionKey = merchant.getSellerSessionKey();
//                LOGGER.debug("saveMachine.SellerId = {},saveMachine.sessionKey = {}",goods.getSellerId(),sessionKey);

                //检查数据库是否添加过
                Inno72MachineDevice inno72MachineDevice = inno72MachineDeviceService.findByMachineCodeAndSellerId(deviceVo.getMachineCode(),sellerId);
                if(StringUtils.isEmpty(deviceVo.getDeviceName())){
                    deviceVo.setStoreName(merchant.getMerchantCode()+"-"+deviceVo.getMachineCode());
                }
                //没有添加过
                if(inno72MachineDevice == null){
                    //根据机器code查询storeId
                    Long storeId = findStores(sellSessionKey,deviceVo.getStoreName());
                    //根据storeId查找deviceCode;
                    String deviceCode = findDeviceByStoreId(sellSessionKey,storeId);
                    if(StringUtils.isEmpty(deviceCode)){
                        //调用淘宝接口
                        deviceCode = saveDevice(sellSessionKey,deviceVo.getStoreName(),storeId,"ANDROID",deviceVo.getMachineCode());
                    }
                    //保存结果信息
                    inno72MachineDevice = new Inno72MachineDevice();
                    inno72MachineDevice.setCreateTime(new Date());
                    inno72MachineDevice.setDeviceCode(deviceCode);
                    inno72MachineDevice.setMachineCode(deviceVo.getMachineCode());
                    inno72MachineDevice.setStoreId(storeId);
                    inno72MachineDevice.setSellerId(merchant.getMerchantCode());
                    inno72MachineDeviceService.save(inno72MachineDevice);
                }
            }
        }
    }

    private void checkDeviceParamVo(DeviceVo vo) {
        if(vo == null) throw new Inno72BizException("参数不能为空");
        if(StringUtils.isEmpty(vo.getGoodsId())) throw new Inno72BizException("goodsId不能为空");
        if(StringUtils.isEmpty(vo.getMachineCode())) throw new Inno72BizException("机器编码不能为空");
    }

    private static String unescape(String escapeStr) {
        String decode = "";
        try {
            decode = URLDecoder.decode(escapeStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LOGGER.info("unescape is {}", decode);
        return decode;
    }
}
