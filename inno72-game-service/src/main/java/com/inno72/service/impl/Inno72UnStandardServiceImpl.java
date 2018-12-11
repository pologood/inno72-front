package com.inno72.service.impl;

import com.inno72.common.RedisConstants;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72UnStandardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class Inno72UnStandardServiceImpl implements Inno72UnStandardService {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MsgUtil msgUtil;

    @Autowired
    private IRedisUtil iRedisUtil;

    @Value("${phoneverificationcode_limit_time}")
    private Integer phoneverificationcodeLimitTime;

    private final String SYMBOLS = "0123456789"; // 数字

    private final Random RANDOM = new SecureRandom();

    @Override
    public void getPhoneVerificationCode(String sessionUuid, String phone) {
        LOGGER.info("getPhoneVerificationCode sessionUuid = {}, phone = {}",sessionUuid,phone);
        String code = getNonceStr();
        //发送短信
        sendSms(phone,code);
        //将code放入redis
        String key = RedisConstants.PHONEVERIFICATIONCODE_REDIS_KEY+sessionUuid +":"+phone;
        iRedisUtil.setex(key,5*60,code);
        //记录redis时间
        key = RedisConstants.PHONEVERIFICATIONCODE_TIME_LIMIT_REDIS_KEY+sessionUuid +":"+phone;
        iRedisUtil.setex(key,60,"1");
        //记录redis次数
        key = RedisConstants.PHONEVERIFICATIONCODE_TIMES_LIMIT_REDIS_KEY+sessionUuid +":"+phone;
        if(iRedisUtil.exists(key)){
            iRedisUtil.incrBy(key,1);
        }else{
            iRedisUtil.incrBy(key,1);
            iRedisUtil.expire(key,phoneverificationcodeLimitTime*60);
        }
    }

    @Override
    public void checkPhoneVerificationCode(String sessionUuid, String phone, String verificationCode) {
        LOGGER.info("checkPhoneVerificationCode sessionUuid = {}, phone = {}, verificationCode ={} ",sessionUuid,phone,verificationCode);
    }

    /**
     * 发送短信验证码
     * @param phone
     * @param code
     */
    private void sendSms(String phone, String code) {
        //yp_validate_code
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("code",code);
        msgUtil.sendSMS("yp_validate_code",params,phone,"inno72-game-service");
    }

    /**
     * 获取长度为 4 的随机数字
     * @return 随机数字
     */
    private String getNonceStr() {

        char[] nonceChars = new char[4];

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }

        return new String(nonceChars);
    }

}
