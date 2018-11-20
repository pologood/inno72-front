package com.inno72.service;

import java.util.List;

public interface Inno72WeChatService {
    List<String> findWeChatQrCodes(String activityId);

    /**
     * 获取微信用户信息
     * @param url 页面url
     * @param code 微信回传code
     * @param merchantCode 微信回传商户code
     * @return
     */
    String findWeChatUserInfo(String url, String code, String merchantCode);
}
