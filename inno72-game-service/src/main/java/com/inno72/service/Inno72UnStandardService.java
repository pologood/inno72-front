package com.inno72.service;

public interface Inno72UnStandardService {
    /**
     * 获取手机验证码
     * @param sessionUuid
     * @param phone
     */
    void getPhoneVerificationCode(String sessionUuid, String phone);

    /**
     * 校验验证码
     * @param sessionUuid
     * @param phone
     * @param verificationCode
     */
    void checkPhoneVerificationCode(String sessionUuid, String phone, String verificationCode);

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
     * @param operatingSystem
     * @param phoneModel
     * @param sacnSoftware
     * @return
     */
    void updatePhoto(String sessionUuid, String photoImg, Integer operatingSystem, String phoneModel, String sacnSoftware);

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
}
