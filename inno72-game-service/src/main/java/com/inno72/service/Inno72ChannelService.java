package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.model.Inno72Machine;
import com.inno72.vo.UserSessionVo;

public interface Inno72ChannelService {
    String buildQrContent(Inno72Machine inno72Machine,String sessionUuid);

    Result<Object> paiYangProcessBeforeLogged(String sessionUuid, UserSessionVo sessionVo, String authInfo,String traceId);

    Result<Object> order(UserSessionVo userSessionVo,String itemId ,String inno72OrderId );

    default void feedBackInTime(String inno72OrderId, String machineCode){
    }
}
