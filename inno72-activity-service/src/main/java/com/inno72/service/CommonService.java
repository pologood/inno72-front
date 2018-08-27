package com.inno72.service;

import com.inno72.vo.Result;

public interface CommonService {
    /**
     * 发送手机验证码
     * @param phone
     * @param code
     */
    Result<Object> sendSMSVerificationCode(String phone, String code);

    /**
     * 校验验证码
     * @param phone
     * @param code
     */
    boolean verificationCode(String phone, String code);
}
