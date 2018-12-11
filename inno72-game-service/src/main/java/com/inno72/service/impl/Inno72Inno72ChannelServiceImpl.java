package com.inno72.service.impl;

import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.common.util.AesUtils;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.utils.StringUtil;
import com.inno72.model.Inno72Machine;
import com.inno72.service.Inno72ChannelService;
import com.inno72.vo.UserSessionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("INNO72")
@Transactional
public class Inno72Inno72ChannelServiceImpl implements Inno72ChannelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72Inno72ChannelServiceImpl.class);

    @Autowired
    private Inno72GameServiceProperties inno72GameServiceProperties;

    @Value("${env}")
    private String env;

    @Override
    public String buildQrContent(Inno72Machine inno72Machine, String sessionUuid) {
        String redirect = inno72GameServiceProperties.get("loginRedirect");
        String url = buildUrl(inno72Machine, redirect,sessionUuid);
        return url;
    }

    @Override
    public Result<Object> paiYangProcessBeforeLogged(String sessionUuid, UserSessionVo sessionVo, String authInfo, String traceId) {
        String userId = FastJsonUtils.getString(authInfo, "phone");
        return null;
    }

    @Override
    public Result<Object> order(UserSessionVo userSessionVo, String itemId, String inno72OrderId) {
        return null;
    }

    private String buildUrl(Inno72Machine inno72Machine, String redirect,String sessionUuid){
        String url = String.format(
                "%s/?sessionUuid=%s&env=%s&channelType=%s",
                redirect, sessionUuid, env, StandardLoginTypeEnum.INNO72.getValue());
        return url;
    }
}
