package com.inno72.service;

import com.inno72.vo.DeviceVo;
import com.taobao.api.ApiException;

import javax.servlet.http.HttpServletResponse;
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

    String deviceVendorFeedback(String sessionKey, String tradeNo, String deviceCode, String itemId, String opTime,String userNick,String merchantName,String merchantCode) throws ApiException;

    void saveMachine(String merchantCode, String machineCode) throws ApiException;

    /**
     * 新增机器
     */
    void saveMachine(List<DeviceVo> list) throws Exception;

    /**
     * 调度中心调用淘宝定时回流
     * @param tradeNo
     * @param deviceCode
     * @param itemId
     * @param opTime
     * @param userNick
     * @param merchantName
     * @param merchantCode
     */
    void feedBackOrder(String tradeNo, String deviceCode, String itemId, String opTime, String userNick, String merchantName, String merchantCode) throws ApiException;

    /**
     * 实时回流
     */
    void feedBackInTime(String inno72OrderId,String machineCode);
}
