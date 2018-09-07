package com.inno72.wechat.controller;

import com.google.gson.Gson;
import com.inno72.wechat.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
 
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatController.class);
    @Autowired
    private WxMpService wxMpService;
    @Value("${wechat.domain}")
    private String domain;

    @Autowired
    private TeamService teamService;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){
        String url = "http://"+domain+"/wechat/userInfo";
        String redirectURL = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl));
        log.info("【微信网页授权】获取code,redirectURL={}", redirectURL);
        return "redirect:" + redirectURL;
    }

    public static void main(String[] args) {
        System.out.println(URLEncoder.encode("https://h5.heat.inno72.com/?pageto=get_score_panel&taskId=5b8fa45b92b83d31125f629c"));
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                         @RequestParam("state") String returnUrl) throws Exception {
        log.info("【微信网页授权】code={}", code);
        log.info("【微信网页授权】state={}", returnUrl);
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        WxMpUser user =null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            //获取用户详情
            user = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken,null);
            log.info("微信网页授权】tokenObj={}",new Gson().toJson(wxMpOAuth2AccessToken));
        } catch (WxErrorException e) {
            log.info("【微信网页授权】{}", e);
            throw new Exception(e.getError().getErrorMsg());
        }
        String userId = teamService.saveUserInfo(user);
        log.info("【微信网页授权】openId={}", wxMpOAuth2AccessToken.getOpenId());
        String redirectUrl = null;
        if(returnUrl.contains("?")){
            redirectUrl = returnUrl + "&userId=" + userId;
        }else{
            redirectUrl = returnUrl + "?userId=" + userId;
        }
        log.info("【微信网页授权】跳转url={}",redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/helloworld")
    @ResponseBody
    public String userInfo(String userId) throws Exception {
        log.info("【helloworld】userId={}", userId);
        return userId;
    }
}
