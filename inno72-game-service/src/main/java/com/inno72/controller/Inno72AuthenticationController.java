package com.inno72.controller;

import javax.annotation.Resource;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.shiro.filter.JWTUtil;
import com.inno72.model.Inno72Authentication;
import com.inno72.model.Inno72User;
import com.inno72.service.Inno72AuthenticationService;
import com.inno72.service.Inno72UserService;

/**
* Created by CodeGenerator on 2018/06/27.
*/
@RestController
@RequestMapping("/authentication")
public class Inno72AuthenticationController {
    @Resource
    private Inno72AuthenticationService inno72AuthenticationService;
    
    @RequestMapping(value = "/login", method = { RequestMethod.POST,  RequestMethod.GET})
    @ResponseBody
    public Result<Object> login(@RequestParam("username") String username,
            @RequestParam("password") String password) {
        
    	Inno72Authentication inno72Authentication = inno72AuthenticationService.getUser(username);
    	 JSONObject jsonObject = new JSONObject();
         if (inno72Authentication.getuPassword().equals(password)) {
             
             jsonObject.put("code", "200");
             jsonObject.put("result", "Login success");
             jsonObject.put("Authorization", JWTUtil.sign(username, password));
             jsonObject.put("isLogin", true);
         } else {
             throw new UnauthorizedException();
         }
         return ResultGenerator.genSuccessResult(jsonObject);
    }
    
}
