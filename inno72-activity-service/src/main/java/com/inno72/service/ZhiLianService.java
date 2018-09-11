package com.inno72.service;

import com.inno72.vo.Result;

public interface ZhiLianService {
    /**
     * 根据userid获取四位验证码
     * @param userId
     * @return
     */
    Result<Object> getVerificationCode(String userId);

    /**
     * 根据验证码获取userid
     * @param code
     * @return
     */
    Result<Object> getUserId(String code);

    /**
     * 根据userid完成游戏
     * @param userId
     * @return
     */
    Result<Object> finish(String userId);

    /**
     * 初始化数据
     * @return
     */
    Result<Object> init();
}
