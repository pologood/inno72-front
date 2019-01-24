package com.inno72.service.impl;

import com.inno72.common.ApplicationContextHandle;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.model.Inno72Order;
import com.inno72.service.Inno72BeidemaService;
import com.inno72.service.Inno72ChannelService;
import com.inno72.service.Inno72GameApiService;
import com.inno72.service.Inno72GameService;
import com.inno72.vo.BeiDeMaRequestVo;
import com.inno72.vo.UserSessionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class Inno72BeidemaServiceImpl implements Inno72BeidemaService {
    @Autowired
    private Inno72GameApiService inno72GameApiService;
    @Autowired
    private Inno72GameService inno72GameService;
    @Override
    public Result order(BeiDeMaRequestVo vo) {
        UserSessionVo userSessionVo = new UserSessionVo(vo.getMachineCode());
        //计算canorder
        Inno72ChannelService channelService = (Inno72ChannelService)ApplicationContextHandle.getBean(StandardLoginTypeEnum.getValue(userSessionVo.getChannelType()));
        Map<String, Object> result = new HashMap<>();
        boolean canOrder = channelService.getCanOrder(userSessionVo);
        if(!canOrder){
            return Results.warn("您已经玩过此游戏",0,1);
        }
        String inno72OrderId = inno72GameApiService.genPaiyangInno72Order(userSessionVo,userSessionVo.getMachineCode(), userSessionVo.getCanOrder(),userSessionVo.getChannelId(), userSessionVo.getActivityPlanId(), userSessionVo.getMachineId(), userSessionVo.getGoodsId(), userSessionVo.getUserId(),
                Inno72Order.INNO72ORDER_GOODSTYPE.PRODUCT);
        userSessionVo.setInno72OrderId(inno72OrderId);
        userSessionVo.setRefOrderId(vo.getOrderNum());
        userSessionVo.setNeedPay(true);

        // 更新第三方订单号进inno72 order
        Result<String> stringResult = inno72GameService
                .updateRefOrderId(inno72OrderId, vo.getOrderNum(), userSessionVo.getUserId());
        return Results.success(0);
    }
}
