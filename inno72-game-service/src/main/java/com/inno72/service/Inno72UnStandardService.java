package com.inno72.service;

import com.inno72.vo.OrderVo;
import com.inno72.vo.WxMpUser;

import java.util.List;

public interface Inno72UnStandardService {
    /**
     * 获取手机验证码
     * @param sessionUuid
     * @param phone
     */
    void getPhoneVerificationCode(String sessionUuid, String phone,Integer type);

    /**
     * 校验验证码
     * @param sessionUuid
     * @param phone
     * @param verificationCode
     */
    String checkPhoneVerificationCode(String sessionUuid, String phone, String verificationCode,Integer operatingSystem,String phoneModel,String sacnSoftware,String clientInfo,Integer type,String openId);

    /**
     * 选择支付方式
     * @param sessionUuid
     * @param payType
     * @return
     */
    String changePayType(String sessionUuid, Integer payType);

    /**
     * 更新用户图片
     * @param sessionUuid
     * @param photoImg
     * @return
     */
    void updatePhoto(String sessionUuid, String photoImg);

    /**
     * 支付回调
     * @param retCode
     * @param billId
     * @param buyerId
     * @param extra
     * @param fee
     * @param outTradeNo
     * @param spId
     * @param terminalType
     * @param type
     */
    void payCallback(String retCode, String billId, String buyerId, String extra, String fee, String outTradeNo, String spId, Integer terminalType, Integer type);

    /**
     * 记录游戏各个点的时间比如 开始游戏时间，结束游戏时间，分享游戏时间
     * @param sessionUuid
     * @param type
     */
    void gamePointTime(String sessionUuid, Integer type);

    /**
     * 获取微信是否关联手机号
     * @param user
     * @return
     */
    String joinPhoneFlag(WxMpUser user);

    /**
     * 我的订单列表
     * @param gameUserId
     * @return
     */
    List<OrderVo> orderList(String gameUserId,Integer pageNum,Integer pageSize);
}
