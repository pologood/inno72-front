package com.inno72.common.util;

import com.alibaba.fastjson.JSONObject;
import com.inno72.mongo.MongoUtil;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import com.inno72.vo.Skin;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FaceCacheUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaceCacheUtil.class);

    private static final String ACTIVITY_KEY = "activity:";

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private MongoUtil mongoUtil;

    @Value("${oss.preuri}")
    private String OSSENDPOINT;



    public void save2RedisFace(String userId, String data) {
        template.opsForValue().set(ACTIVITY_KEY+userId,data);
    }

    public String getFaceFromRedis(String userId) {
        return template.opsForValue().get(ACTIVITY_KEY+userId);
    }

    public void save2MongoFace(String userId, String data) {
        deleteSkinByUserId(userId);
        Skin skin = new Skin();
        JSONObject jsonObject = JSONObject.parseObject(data);

        skin.setAcneLevel(jsonObject.getInteger("acne_level"));
        skin.setAge(jsonObject.getInteger("age"));
        skin.setBlackLevel(jsonObject.getInteger("black_level"));
        skin.setColorLevel(jsonObject.getInteger("color_level"));
        skin.setGender(jsonObject.getInteger("gender"));
        skin.setOilLevel(jsonObject.getInteger("oil_level"));
        skin.setUserId(userId);
        skin.setScore(Skin.calculationScore(skin));
        mongoUtil.save(skin);
    }

    public void deleteSkinByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        mongoUtil.delete(query,Skin.class);
    }

    private Skin findByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<Skin> skins = mongoUtil.find(query,Skin.class);
        if(skins!=null&&skins.size()>0){
            return skins.get(0);
        }
        return null;
    }

    public Result updateSkinPicUrl(String userId, String url) {
        Skin skin = findByUserId(userId);
        if(skin == null){
            LOGGER.info("上传图片记录到mongo失败，根据userId={}找不到记录",userId);
            return Results.failure("请进行检测");
        }
        Skin param = new Skin();
        param.setId(skin.getId());
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("picUrl",url);
        mongoUtil.update(param,map,Skin.class);
        return Results.success();
    }

    public String getSckinChectPicByUserId(String userId) {
        Skin skin = findByUserId(userId);
        if(skin == null||StringUtils.isEmpty(skin.getPicUrl())) return null;
        return OSSENDPOINT + skin.getPicUrl();
    }

    public Skin getFaceFromMongo(String userId) {
        Skin skin = findByUserId(userId);
        if(skin != null) {
            //查询总人数
            Query query = new Query();
            long sum = mongoUtil.count(query,Skin.class);

            query.addCriteria(Criteria.where("score").lt(skin.getScore()));
            long ltsum = mongoUtil.count(query,Skin.class);
            DecimalFormat df=new DecimalFormat("0.00");
            String percent = df.format(ltsum*100/(float)sum);
            skin.setPercent(percent);
        }
        return skin;
    }

}
