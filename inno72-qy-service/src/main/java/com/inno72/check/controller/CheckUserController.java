package com.inno72.check.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckUserService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.SessionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RequestMapping(value = "/check/user")
@RestController
@CrossOrigin
public class CheckUserController {

    @Resource
    private CheckUserService checkUserService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 验证码
     */
    @RequestMapping(value="/smsCode", method = {RequestMethod.POST})
    @ResponseBody
    public Result<String> smsCode(@RequestBody Inno72CheckUser inno72CheckUser){
        logger.info("获取验证码H5接口参数：{}",JSON.toJSON(inno72CheckUser));
        Result<String> result = checkUserService.smsCode(inno72CheckUser.getPhone());
        logger.info("获取验证码H5接口结果：{}",JSON.toJSON(result));
        return result;
    }

    /**
     * 登录
     */
    @RequestMapping(value="/login", method = {RequestMethod.POST})
    public Result<SessionData> login(@RequestBody Inno72CheckUser inno72CheckUser){
        logger.info("登录H5接口参数：{}",JSON.toJSON(inno72CheckUser));
        Result<SessionData> result = checkUserService.login(inno72CheckUser.getPhone(),inno72CheckUser.getSmsCode());
        logger.info("登录H5接口结果：{}",JSON.toJSON(result));
        return result;
    }

    /**
     * 上传头像
     */
    @RequestMapping(value="/upload")
    public Result<String> upload(MultipartFile file){
        logger.info("上传头像H5接口开始。。。");
        Result<String> result = checkUserService.upload(file);
        logger.info("上传头像H5接口返回结果{}", JSON.toJSON(result));
        return result;
    }

    /**
     * 编辑用户
     */
    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public Result<Inno72CheckUser> update(@RequestBody Inno72CheckUser inno72CheckUser){
        logger.info("编辑用户H5接口参数：{}",JSON.toJSON(inno72CheckUser));
        Result<Inno72CheckUser> result = checkUserService.updateUser(inno72CheckUser);
        logger.info("编辑用户H5接口返回：{}",JSON.toJSON(inno72CheckUser));
        return result;
    }


    @RequestMapping(value = "logout")
    public Result<String> logout(){
        return checkUserService.logout();
    }
}
