package com.inno72.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.service.Inno72AuthenticationService;

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

    	return inno72AuthenticationService.login(username,password);
    }
    
}
