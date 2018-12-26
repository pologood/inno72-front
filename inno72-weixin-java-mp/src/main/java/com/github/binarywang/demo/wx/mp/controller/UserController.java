package com.github.binarywang.demo.wx.mp.controller;

import com.github.binarywang.demo.wx.mp.config.WxMpConfiguration;
import com.github.binarywang.demo.wx.mp.service.GameServcie;
import com.github.binarywang.demo.wx.mp.vo.Result;
import com.github.binarywang.demo.wx.mp.vo.WinXinEntity;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
@Slf4j
public class UserController {

    @Autowired
    private GameServcie gameService;

//    public final static String appid= "wx112e5d49fcce8a44";
    @RequestMapping("/")
    public String menuCreate() throws WxErrorException {
        return "ok";
    }

    @RequestMapping(value = "/redirectAdapter", method = RequestMethod.GET)
    public void redirectAdapter(HttpServletResponse response, String code, String appid) throws Exception {
        log.info("code={}",code);
        WxMpService wxMpService = WxMpConfiguration.getMpServices().get(appid);
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        //获取用户详情
        WxMpUser user = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken,null);
        Gson gson = new Gson();
        log.info("user = {}",gson.toJson(user));
        String url = gameService.redirectAdapter(user);
        log.info("url = {}",url);
        response.sendRedirect(url);
    }

    @RequestMapping(value = "/user")
    public WinXinEntity getWeChat(String url,String code,String appid){
        try{
            log.info("url={},code = {}",url,code);
            final WxMpService wxMpService = WxMpConfiguration.getMpServices().get(appid);
//            wxMpService.getJsapiTicket();
            WxJsapiSignature signature = wxMpService.createJsapiSignature(url);

            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            //获取用户详情
            WxMpUser user = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken,null);
            String accessToken = wxMpService.getAccessToken();
            log.info("accessToken = {}",accessToken);
            WinXinEntity w = new WinXinEntity();
            w.setSignature(signature);
            w.setUser(user);
            Gson g = new Gson();
            log.info("getWeChat result = {}",g.toJson(w));
            return w;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/getSignature")
    public WxJsapiSignature getSignature(String url){
        try{
            String appid = "wxd2d020e170a05549";
            log.info("url={}",url,appid);
            final WxMpService wxMpService = WxMpConfiguration.getMpServices().get(appid);
            WxJsapiSignature signature = wxMpService.createJsapiSignature(url);
            return signature;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/getUserByOpenId")
    public Result getUserByOpenId(String openId){
        Result r = new Result();
        try{
            String appid = "wxd2d020e170a05549";
            log.info("getUserByOpenId openId={}",openId);
            final WxMpService wxMpService = WxMpConfiguration.getMpServices().get(appid);
            WxMpUser user = wxMpService.getUserService().userInfo(openId);
            r.setData(user);
        }catch(Exception e){
            e.printStackTrace();
            r.setCode(Result.FAILURE);
            r.setMsg(e.getMessage());
        }
        return r;
    }
}
