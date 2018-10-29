package com.inno72.service.impl;

import com.inno72.common.Inno72BizException;
import com.inno72.common.json.JsonUtil;
import com.inno72.mapper.*;
import com.inno72.model.Inno72FeedBackLog;
import com.inno72.model.Inno72Goods;
import com.inno72.model.Inno72MachineDevice;
import com.inno72.model.Inno72Merchant;
import com.inno72.service.Inno72LocalDataSendService;
import com.inno72.service.Inno72NewretailService;
import com.inno72.vo.OrderOrderGoodsVo;
import com.taobao.api.ApiException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Inno72LocalDataSendServiceImpl implements Inno72LocalDataSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72InteractMachineTimeServiceImpl.class);
    @Autowired
    Inno72MerchantMapper inno72MerchantMapper;
    @Autowired
    Inno72GoodsMapper inno72GoodsMapper;
    @Autowired
    Inno72OrderGoodsMapper inno72OrderGoodsMapper;
    @Autowired
    Inno72OrderMapper inno72OrderMapper;
    @Autowired
    Inno72FeedBackLogMapper inno72FeedBackLogMapper;
    @Autowired
    Inno72NewretailService inno72NewretailService;
    @Autowired
    Inno72MachineMapper inno72MachineMapper;
    @Autowired
    Inno72GameUserChannelMapper inno72GameUserChannelMapper;

    @Autowired
    Inno72MachineDeviceMapper inno72MachineDeviceMapper;

    @Value("${sell_session_key}")
    private String sellSessionKey;

    private static Map<String,String> deviceCodeMap = new HashMap<String,String>();
    @Transactional
    @Override
    public void datasend(String[] merchantNames) throws ApiException {
        Map<String,Integer> devicelist = new HashMap<String,Integer>();
        LOGGER.debug("datasend start...");
        for(String merchantName:merchantNames){
            //查找商户id
            int size = 0;
            List<Inno72Merchant> merchantList = inno72MerchantMapper.findMerchantByName(merchantName);
            if(merchantList == null || merchantList.size()==0){
                LOGGER.error("merchantName = {},无法找到商户",merchantName);
                continue;
            }
            for(Inno72Merchant merchant:merchantList){

                List<OrderOrderGoodsVo> list = findSuccessOrderByMerchantId(merchant.getId());
                if(list!=null&&list.size()>0){
                    for(int i=0;i<list.size();i++){
                        OrderOrderGoodsVo orderOrderGoodsVo = list.get(i);
                        //获取userNick
                        String userNick = inno72GameUserChannelMapper.selectUserNickByGameUserId(orderOrderGoodsVo.getUserId());
                        size++;
                        Inno72FeedBackLog log = null;//findLogByOrderId(orderOrderGoodsVo.getOrderId());
                        if(log== null){
                            //查找deviceCode
                            String deviceCode = findDeviceCode(merchant.getMerchantCode(),orderOrderGoodsVo.getMachineCode());
                            if(deviceCode == null){
                                LOGGER.error("无法找到deviceCode, sellerId = {},machineCode = {}",merchant.getMerchantCode(),orderOrderGoodsVo.getMachineCode());
                                devicelist.put(""+merchant.getMerchantCode()+"_"+orderOrderGoodsVo.getMachineCode(),1);
                                throw new Inno72BizException("无法找到deviceCode");
                            }
                            if(!StringUtils.isEmpty(userNick)){
                                //调用淘宝回流
                                inno72NewretailService.deviceVendorFeedback(sellSessionKey,orderOrderGoodsVo.getTaobaoOrderNum(),deviceCode,orderOrderGoodsVo.getTaobaoGoodsId(),"2018-11-01 00:00:00",userNick,merchant.getMerchantName(),merchant.getMerchantCode());
                                System.out.println(size);
                            }else{
                                LOGGER.error("userNick is null 不回流接口 orderId = {}",orderOrderGoodsVo.getOrderId());
                            }
                        }
                    }
                }
            }
            System.out.println(JsonUtil.toJson(devicelist));
            System.out.println(merchantName+":"+size);
        }

    }
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void saveLog(Inno72FeedBackLog log) {
        inno72FeedBackLogMapper.insert(log);
        System.out.println(JsonUtil.toJson(log));
    }
    @Transactional
    public List<OrderOrderGoodsVo> findSuccessOrderByMerchantId(String merchantId) {
        return inno72OrderMapper.findSuccessOrderByMerchantId(merchantId);
    }
    @Transactional
    public String findDeviceCode(String sellerId, String machineCode) {
        LOGGER.debug("findDeviceCode sellerId = {},machineCode = {}",sellerId,machineCode);
        String key = sellerId+machineCode;
        String deviceCode = deviceCodeMap.get(key);
        if(deviceCode == null){
            Inno72MachineDevice device = new Inno72MachineDevice();
            device.setSellerId(sellerId);
            device.setMachineCode(machineCode);
            device = inno72MachineDeviceMapper.selectOne(device);
            if(device == null) return null;
            deviceCode = device.getDeviceCode();
            deviceCodeMap.put(key,deviceCode);
        }
        return deviceCode;
    }
    @Transactional
    public Inno72FeedBackLog findLogByOrderId(String orderId) {
        Inno72FeedBackLog log = new Inno72FeedBackLog();
        log.setOrderId(orderId);
        List<Inno72FeedBackLog> list = inno72FeedBackLogMapper.select(log);
        if(list == null || list.size() == 0) return null;
        return list.get(0);
    }

    @Transactional
    public List<Inno72Goods> findGoodsBySellerId(String sellerId) {
        Inno72Goods param = new Inno72Goods();
        param.setSellerId(sellerId);
        return inno72GoodsMapper.select(param);
    }
}
