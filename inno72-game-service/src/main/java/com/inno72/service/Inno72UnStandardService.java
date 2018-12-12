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
}
