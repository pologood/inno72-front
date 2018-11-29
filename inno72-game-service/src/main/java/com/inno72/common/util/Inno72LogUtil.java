package com.inno72.common.util;

import com.inno72.vo.UserSessionVo;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
public class Inno72LogUtil {
    @Autowired
    private GameSessionRedisUtil gameSessionRedisUtil;
    public static final Logger logger = LoggerFactory.getLogger(Inno72LogUtil.class);
    public void info(String sessionUuid,String format, Object... arguments){
        try {
            Message message = buildMessage(sessionUuid, format, arguments);
            logger.info(message.getFormattedMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    private Message buildMessage(String sessionUuid,String format, Object... arguments) {
        Message message = null;
        UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
        if (userSessionVo == null) {
            logger.info("会话不存在!");
            return new FormattedMessage(format,(Object[])arguments);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("traceid={},machineCode={},userId={}");
        if(!StringUtils.isEmpty(format)){
            sb.append(",");
            sb.append(format);
        }
        List<Object> param = new ArrayList<>();
        param.add(userSessionVo.getTraceId());
        param.add(userSessionVo.getMachineCode());
        param.add(userSessionVo.getUserId());

        if(arguments!=null&&arguments.length>0){
            Object[] obj =  (Object[])arguments;
            param.addAll(Arrays.asList(obj));
        }
        message = new FormattedMessage(sb.toString(),param.toArray());
        return message;
    }
}
