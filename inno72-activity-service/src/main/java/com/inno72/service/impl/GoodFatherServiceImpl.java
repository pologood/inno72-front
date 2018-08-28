package com.inno72.service.impl;

import com.inno72.mongo.MongoUtil;
import com.inno72.service.GoodFatherService;
import com.inno72.vo.GoodFather;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class GoodFatherServiceImpl implements GoodFatherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodFatherService.class);
    @Autowired
    private MongoUtil mongoUtil;

    @Override
    public Result<Object> save(GoodFather goodFather) {
        if(findByPhone(goodFather.getPhone())==null){
            goodFather.setCreateTime(new Date());
            goodFather.setStatus(GoodFather.STATUS_UNATTEND);
            mongoUtil.save(goodFather);
        }
        return Results.success();
//        else{
//            LOGGER.info("该手机号已经参加游戏,phone={}",goodFather.getPhone());
//            return Results.warn("该手机号已经参加游戏",-2);
//        }
    }

    @Override
    public Result<Object> attend(String phone) {
        GoodFather goodFather = findByPhone(phone);
        if(goodFather==null){
            LOGGER.info("该手机号未填写个人信息,phone={}",phone);
            return Results.warn("该手机号未填写个人信息",-2);
        }
        if(goodFather.getStatus()==GoodFather.STATUS_UNATTEND){
            updateStatusByPhone(phone,GoodFather.STATUS_IN_LOTTERY_DRAW);
            return Results.success();
        }else{
            return Results.failure("该手机号已经参加抽奖");
        }
    }

    private void updateStatusByPhone(String phone, Integer status) {
        GoodFather param = new GoodFather();
        param.setPhone(phone);
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("status",status);
        mongoUtil.update(param,map,GoodFather.class);
    }

    @Override
    public Result<Object> getlotteryDrawResult(String phone) {
        GoodFather goodFather = findByPhone(phone);
        if(goodFather==null||goodFather.getStatus()!=GoodFather.STATUS_HIT){
            return Results.success(0);
        }
        return Results.success(1);
    }


    private GoodFather findByPhone(String phone) {
        Query query = new Query();
        query.addCriteria(Criteria.where("phone").is(phone));
        List<GoodFather> goodFathers = mongoUtil.find(query,GoodFather.class);
        if(goodFathers!=null&&goodFathers.size()>0){
            return goodFathers.get(0);
        }
        return null;
    }
}
