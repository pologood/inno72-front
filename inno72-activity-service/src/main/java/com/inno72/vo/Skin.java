package com.inno72.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Skin {

    private static final Logger LOGGER = LoggerFactory.getLogger(Skin.class);
    @Id
    public String id;
    public Integer gender;//0为男性1为女性
    public Integer age;//年龄
    public Integer colorLevel;//肤色等级，0~7，数字越小越白
//    public Integer hueLevel;//色调等级，0~2，冷色，中性，暖色",
    public Integer oilLevel;//总体出油情况 总体出油情况, 0-干性，1-偏干，2-中性，3-混合油性，4-偏油，5-油性
    public Integer acneLevel ;//痘痘严重程度，0~3，0表示没有，1~3表示轻度、中度、重度 ",
    public Integer blackLevel;//黑头严重程度，0~3，0表示没有，1~3表示轻度、中度、重度
    public Integer score;//评分
    public String userId;
    public String picUrl;
    @Transient
    public String percent;//打败人数百分比

    public static Integer calculationScore(Skin skin) {
        int age = skin.getAge();
        int colorLevel = skin.getColorLevel();
        int oilLevel = skin.getOilLevel();
        int acneLevel = skin.getAcneLevel();
        int blackLevel = skin.getBlackLevel();
        int score = 0;

        score+=20;
        if(age == -1){
            age = 28;
        }
        if(age > 26){

            score = score -(age - 26);
            if(score<13) score = 13;
        }

        if(colorLevel == 0){
            //白皙
            score+=20;
        }else if(colorLevel == 1 || colorLevel == 2){
            //较白
            score+=19;
        }else if(colorLevel == 3 || colorLevel == 4){
            //中等
            score+=18;
        }else if(colorLevel == 5 || colorLevel == 6){
            //较深
            score+=17;
        }else if(colorLevel == 7){
            //黝黑
            score+=16;
        }else{
            LOGGER.info("colorLevel 异常 colorLevel={}",colorLevel);
            score+=18;
        }

        if(oilLevel == 0){
            //0-干性，
            score+=17;
        }else if(oilLevel == 1){
            //1-偏干，
            score+=18;
        }else if(oilLevel == 2){
            //2-中性，
            score+=20;
        }else if(oilLevel == 3){
            //3-混合油性，
            score+=19;
        }else if(oilLevel == 4){
            //4-偏油，
            score+=18;
        }else if(oilLevel == 5){
            //5-油性
            score+=17;
        }else{
            score+=19;
            LOGGER.info("oilLevel 异常 oilLevel={}",colorLevel);
        }


        if(acneLevel == 0){
            //0~3，0表示没有，
            score+=20;
        }else if(acneLevel == 1){
            //轻度、
            score+=19;
        }else if(acneLevel == 2){
            //中度、
            score+=18;
        }else if(acneLevel == 3){
            //重度 ",
            score+=17;
        }else{
            score+=18;
            LOGGER.info("acneLevel 异常 acneLevel={}",acneLevel);
        }

        //黑头严重程度
        if(blackLevel == 0){
            //0~3，0表示没有，
            score+=20;
        }else if(blackLevel == 1){
            //轻度、
            score+=19;
        }else if(blackLevel == 2){
            //中度、
            score+=18;
        }else if(blackLevel == 3){
            //重度 ",
            score+=17;
        }else{
            //重度 ",
            score+=18;
            LOGGER.info("blackLevel 异常 blackLevel={}",blackLevel);
        }
        return score;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getColorLevel() {
        return colorLevel;
    }

    public void setColorLevel(Integer colorLevel) {
        this.colorLevel = colorLevel;
    }

    public Integer getOilLevel() {
        return oilLevel;
    }

    public void setOilLevel(Integer oilLevel) {
        this.oilLevel = oilLevel;
    }

    public Integer getAcneLevel() {
        return acneLevel;
    }

    public void setAcneLevel(Integer acneLevel) {
        this.acneLevel = acneLevel;
    }

    public Integer getBlackLevel() {
        return blackLevel;
    }

    public void setBlackLevel(Integer blackLevel) {
        this.blackLevel = blackLevel;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
