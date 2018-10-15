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
                //查找商户下的商品
                List<Inno72Goods> goodsList = findGoodsBySellerId(merchant.getId());

                if(goodsList !=null && goodsList.size()>0){
                    for(Inno72Goods goods:goodsList){
                        //查找商品对应的出货成功的订单
                        List<OrderOrderGoodsVo> list = findSuccessOrderByGoodsId(goods.getId(),goods.getCode());
                        if(list!=null&&list.size()>0){
//                            System.out.println(JsonUtil.toJson(list));
                            size += list.size();
                            for(OrderOrderGoodsVo orderOrderGoodsVo:list){
                                Inno72FeedBackLog log = findLogByOrderId(orderOrderGoodsVo.getOrderId());
                                if(log== null){
                                    //查找deviceCode
                                    String deviceCode = findDeviceCode(merchant.getMerchantCode(),orderOrderGoodsVo.getMachineCode());
                                    if(deviceCode == null){
                                        LOGGER.error("无法找到deviceCode, sellerId = {},machineCode = {}",orderOrderGoodsVo.getSellerId(),orderOrderGoodsVo.getMachineCode());
                                        throw new Inno72BizException("无法找到deviceCode");
                                    }
                                    //调用淘宝回流
                                    String body = inno72NewretailService.deviceVendorFeedback("6100816bd6f85638abd2fdae18beee05e32809cebf39e224008390433",orderOrderGoodsVo.getTaobaoOrderNum(),"tmall_trade",deviceCode,"SHIP_CNT",orderOrderGoodsVo.getTaobaoGoodsId(),"2018-10-28 00:00:00");
                                    //插入日志
                                    log.setGoodsId(orderOrderGoodsVo.getGoodsId());
                                    log.setOrderId(orderOrderGoodsVo.getOrderId());
                                    log.setMerchantName(merchantName);
                                    log.setResponseBody(body);
                                    inno72FeedBackLogMapper.insert(log);
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(merchantName+":"+size);
        }

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

    private List<OrderOrderGoodsVo> findSuccessOrderByGoodsId(String goodsId,String taobaoGoodsCode) {
        Inno72OrderGoods param = new Inno72OrderGoods();
        param.setGoodsId(goodsId);
        List<Inno72OrderGoods> list = inno72OrderGoodsMapper.select(param);
        List<OrderOrderGoodsVo> returnList = new ArrayList<OrderOrderGoodsVo>();
        if(list != null && list.size()>0 ){
            for(Inno72OrderGoods inno72OrderGoods:list){
                Inno72Order order = inno72OrderMapper.selectByPrimaryKey(inno72OrderGoods.getOrderId());
                if(1 == order.getGoodsStatus()){
                    String sellerId = inno72MerchantMapper.selectByPrimaryKey(order.getMerchantId()).getMerchantCode();
                    Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(order.getMachineId());
                    //出货成功
                    OrderOrderGoodsVo vo = new OrderOrderGoodsVo();
                    vo.setChannelId(order.getChannelId());
                    vo.setGoodsId(goodsId);
                    vo.setOrderId(order.getId());
                    vo.setOrderNum(order.getOrderNum());
                    vo.setTaobaoGoodsId(taobaoGoodsCode);
                    vo.setTaobaoOrderNum(order.getRefOrderId());
                    vo.setUserId(order.getUserId());
                    vo.setSellerId(sellerId);
                    vo.setMachineCode(inno72Machine.getMachineCode());
//                    vo.setUserNick(order.get);
                    returnList.add(vo);
                }
            }
        }
        return returnList;
    }

    private List<Inno72Goods> findGoodsBySellerId(String sellerId) {
        Inno72Goods param = new Inno72Goods();
        param.setSellerId(sellerId);
        return inno72GoodsMapper.select(param);
    }
}
