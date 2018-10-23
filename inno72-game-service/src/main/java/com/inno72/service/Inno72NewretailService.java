package com.inno72.service;

import com.inno72.vo.DeviceVo;
import com.taobao.api.ApiException;

import java.util.List;

/**
 * 新零售服务
 */
public interface Inno72NewretailService {
    Long findStores(String sessionKey, String storeName) throws Exception;

    String saveDevice(String sessionKey, String deviceName, Long storeId, String osType, String outerCode) throws Exception;

    Boolean getMemberIdentity(String sessionKey, String mixNick) throws ApiException;

    String getStoreMemberurl(String sessionKey, String deviceCode, String callbackUrl) throws ApiException;

    String deviceVendorFeedback(String sessionKey, String tradeNo, String tradeType, String deviceCode, String action
            , String itemId, String couponId, String userNick, String outerBizId, String opTime, String outerUser) throws ApiException;

    String deviceVendorFeedback(String sessionKey, String tradeNo, String deviceCode, String itemId, String opTime) throws ApiException;

    /**
     * 新增机器
     */
    void saveMachine(List<DeviceVo> list) throws Exception;
}
