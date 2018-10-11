package com.inno72.service;

import com.taobao.api.ApiException;

/**
 * 新零售服务
 */
public interface Inno72NewretailService {
    Long findStores(String sessionKey, String storeName) throws Exception;

    String saveDevice(String sessionKey, String deviceName, Long storeId, String osType, String deviceType, String outerCode) throws Exception;

    Boolean getMemberIdentity(String sessionKey, String mixNick) throws ApiException;

    String getStoreMemberurl(String sessionKey, String deviceCode) throws ApiException;

    void deviceVendorFeedback(String sessionKey, String tradeNo, String tradeType, String deviceCode, String action
            , String itemId, String couponId, String userNick, String outerBizId, String opTime, String outerUser) throws ApiException;
}
