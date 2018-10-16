package com.inno72.service.impl;

import com.inno72.common.Inno72BizException;
import com.inno72.common.json.JsonUtil;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.service.Inno72LocalDataSendService;
import com.inno72.service.Inno72NewretailService;
import com.inno72.vo.OrderOrderGoodsVo;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.ArrayList;
import java.util.List;

@Transactional
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
    Inno72MachineDeviceMapper inno72MachineDeviceMapper;
    @Override
    public void datasend(String[] merchantNames) throws ApiException {
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
//                            System.out.println(JsonUtil.toJson(list));
                    size += list.size();
                    for(OrderOrderGoodsVo orderOrderGoodsVo:list){
                        Inno72FeedBackLog log = findLogByOrderId(orderOrderGoodsVo.getOrderId());
                        if(log== null){
                            //查找deviceCode
                            String deviceCode = findDeviceCode(merchant.getMerchantCode(),orderOrderGoodsVo.getMachineCode());
                            if(deviceCode == null){
                                LOGGER.error("无法找到deviceCode, sellerId = {},machineCode = {}",merchant.getMerchantCode(),orderOrderGoodsVo.getMachineCode());
                                throw new Inno72BizException("无法找到deviceCode");
                            }
                            //调用淘宝回流
                            String body = inno72NewretailService.deviceVendorFeedback("6100816bd6f85638abd2fdae18beee05e32809cebf39e224008390433",orderOrderGoodsVo.getTaobaoOrderNum(),"tmall_trade",deviceCode,"SHIP_CNT",orderOrderGoodsVo.getTaobaoGoodsId(),"2018-10-28 00:00:00");
                            //插入日志
                            log = new Inno72FeedBackLog();
                            log.setGoodsId(orderOrderGoodsVo.getGoodsId());
                            log.setOrderId(orderOrderGoodsVo.getOrderId());
                            log.setMerchantName(merchantName);
                            log.setResponseBody(body);
                            saveLog(log);
                        }
                    }
                }
            }
            System.out.println(merchantName+":"+size);
        }

    }
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void saveLog(Inno72FeedBackLog log) {
        inno72FeedBackLogMapper.insert(log);
        System.out.println(JsonUtil.toJson(log));
    }

    private List<OrderOrderGoodsVo> findSuccessOrderByMerchantId(String merchantId) {
        return inno72OrderMapper.findSuccessOrderByMerchantId(merchantId);
    }

    private String findDeviceCode(String sellerId, String machineCode) {
        LOGGER.debug("findDeviceCode sellerId = {},machineCode = {}",sellerId,machineCode);
        Inno72MachineDevice device = new Inno72MachineDevice();
        device.setSellerId(sellerId);
        device.setMachineCode(machineCode);
        device = inno72MachineDeviceMapper.selectOne(device);
        if(device == null) return null;
        return device.getDeviceCode();
    }

    private Inno72FeedBackLog findLogByOrderId(String orderId) {
        Inno72FeedBackLog log = new Inno72FeedBackLog();
        log.setOrderId(orderId);
        List<Inno72FeedBackLog> list = inno72FeedBackLogMapper.select(log);
        if(list == null || list.size() == 0) return null;
        return list.get(0);
    }


    private List<Inno72Goods> findGoodsBySellerId(String sellerId) {
        Inno72Goods param = new Inno72Goods();
        param.setSellerId(sellerId);
        return inno72GoodsMapper.select(param);
    }
}
