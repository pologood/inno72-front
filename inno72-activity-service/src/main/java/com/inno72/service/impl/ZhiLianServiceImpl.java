package com.inno72.service.impl;

import com.inno72.common.util.Inno72StringUtil;
import com.inno72.mongo.MongoUtil;
import com.inno72.service.ZhiLianService;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import com.inno72.vo.ZhiLianUser;
import com.inno72.vo.ZhiLianVerifivation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ZhiLianServiceImpl implements ZhiLianService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Set<String> codes = new HashSet<String>(2000);

    @Autowired
    private MongoUtil mongoUtil;

    @Override
    public Result<Object> getVerificationCode(String userId) {
        LOGGER.info("获取验证码userId={}",userId);

        //查看是否玩过
        ZhiLianUser user = getUser(userId);
        if(user == null){
            //获取可用的code
            String code = getUsableVerificationCode();
            LOGGER.info("获取验证码CODE，userId={},code={}",userId,code);
            
            saveUser(userId,code);
            return Results.success(code);
        }else {
            
            if(user.getStatus() == ZhiLianUser.STATUS_DOWN){
                LOGGER.info("获取验证码此用户已经玩过此游戏，userId={}",userId);
                return Results.failure("您已经玩过此游戏");
            }else{
                return Results.success(user.getCode());
            }
        }
    }

    private void saveUser(String userId, String code) {
        LOGGER.info("保存用户userId={},code={}",userId,code);
        ZhiLianUser user = new ZhiLianUser();
        user.setCode(code);
        user.setCreateTime(System.currentTimeMillis());
        user.setUserId(userId);
        user.setStatus(ZhiLianUser.STATUS_UNDOWN);
        mongoUtil.save(user);
    }

    private ZhiLianUser getUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoUtil.findOne(query,ZhiLianUser.class);
    }

    /**
     * 获取可用的code
     * @return
     */
    private String getUsableVerificationCode() {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(ZhiLianVerifivation.STATUS_USABLE)).limit(1);
        ZhiLianVerifivation code = mongoUtil.findOne(query,ZhiLianVerifivation.class);

        //更新为不可用
        ZhiLianVerifivation param = new ZhiLianVerifivation();
        param.setId(code.getId());
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("status",ZhiLianVerifivation.STATUS_UNUSABLE);
        mongoUtil.update(param,map,ZhiLianVerifivation.class);

        return code.getCode();
    }

    @Override
    public Result<Object> getUserId(String code) {
        ZhiLianUser user = getUserByCode(code);
        if(user!=null){
            if(user.getStatus() == ZhiLianUser.STATUS_DOWN){
                return Results.failure("您已经玩过此游戏");
            }else{
                return Results.success(user.getUserId());
            }
        }else{
            return Results.failure("验证码错误");
        }
    }

    private ZhiLianUser getUserByCode(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        return mongoUtil.findOne(query,ZhiLianUser.class);
    }

    @Override
    public Result<Object> finish(String userId) {
        LOGGER.info("完成游戏userId={}",userId);
        ZhiLianUser param = new ZhiLianUser();
        param.setUserId(userId);
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("status",ZhiLianUser.STATUS_DOWN);
        mongoUtil.update(param,map,ZhiLianUser.class);
        return Results.success();
    }

    @Override
    public Result<Object> init() {
        LOGGER.info("初始化数据开始");
        long start = System.currentTimeMillis();
        while (codes.size()<2001){
            String code = Inno72StringUtil.genVerificationCode();
            if(!codes.contains(code)){
                ZhiLianVerifivation verifivation = new ZhiLianVerifivation();
                verifivation.setCode(code);
                verifivation.setStatus(ZhiLianVerifivation.STATUS_USABLE);
                mongoUtil.save(verifivation);
                codes.add(code);
            }
        }
        codes = null;
        long end = System.currentTimeMillis();
        LOGGER.info("初始化完成，用时：{}毫秒",end-start);
        return Results.success("初始化完成，用时："+(end-start)+"毫秒");
    }

}
