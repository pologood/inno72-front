package com.inno72.service.impl;

import com.inno72.common.Inno72BizException;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.service.Inno72NewretailService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Service
@Transactional
public class Inno72NewretailServiceImpl implements Inno72NewretailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72NewretailServiceImpl.class);
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
     * @param deviceType 设备类型：
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
    public String saveDevice(String sessionKey, String deviceName, Long storeId, String osType, String deviceType, String outerCode) throws Exception {
        SmartstoreDeviceAddRequest req = new SmartstoreDeviceAddRequest();
        req.setDeviceName(deviceName);
        req.setStoreId(storeId);
        req.setOsType(osType);
        req.setDeviceType(deviceType);
        req.setOuterCode(outerCode);
        SmartstoreDeviceAddResponse rsp = client.execute(req, sessionKey);
        LOGGER.debug("saveDevice deviceName ={},storeId = {},osType = {},deviceType = {},outerCode = {},response = {}",deviceName,storeId,osType,deviceType,outerCode,rsp.getBody());
        if(rsp.getErrorCode()!=null){
            LOGGER.error("saveDevice error deviceName ={},storeId = {},osType = {},deviceType = {},outerCode = {},response = {}",deviceName,storeId,osType,deviceType,outerCode,rsp.getBody());
            throw new Inno72BizException("保存设备天猫返回错误："+rsp.getSubMessage());
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
//    public static void findDeviceByStoreId(String sessionKey,Long storeId) throws ApiException {
//        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
//        SmartstoreDeviceQueryRequest req = new SmartstoreDeviceQueryRequest();
//        req.setStoreId(storeId);
//        req.setPageNum(1L);
//        req.setPageSize(20L);
//        SmartstoreDeviceQueryResponse rsp = client.execute(req, sessionKey);
//        System.out.println(rsp.getBody());
//    }

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
    public String getStoreMemberurl(String sessionKey, String deviceCode) throws ApiException {
        TmallDeviceBrandMemberurlGetRequest req = new TmallDeviceBrandMemberurlGetRequest();
        req.setDeviceCode(deviceCode);
        req.setLongterm(true);
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
    public void deviceVendorFeedback(String sessionKey, String tradeNo, String tradeType, String deviceCode, String action
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
