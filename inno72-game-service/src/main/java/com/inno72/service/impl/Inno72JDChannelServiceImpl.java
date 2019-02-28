package com.inno72.service.impl;

import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.model.Inno72Machine;
import com.inno72.service.Inno72ChannelService;
import com.inno72.vo.StandardPrepareLoginReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("JD")
@Transactional
public class Inno72JDChannelServiceImpl  implements Inno72ChannelService {

    @Autowired
    private Inno72GameServiceProperties inno72GameServiceProperties;

    @Override
    public String buildQrContent(Inno72Machine inno72Machine, String sessionUuid, StandardPrepareLoginReqVo req) {
        String gameServerUrl = inno72GameServiceProperties.get("gameServerUrl");
        String redirectUrl = gameServerUrl +"/api/activity/jdredirect?machineCode="+req.getMachineCode();
        return redirectUrl;
    }
}
