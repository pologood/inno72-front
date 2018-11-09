package com.inno72.wechat.controller;

import com.google.gson.Gson;
import com.inno72.plugin.http.HttpClient;
import com.inno72.wechat.vo.WechatQRCode;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/wechatqr")
public class QrController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrController.class);

    @Autowired
    private WxMpService wxMpService;
    /**
     * 创建临时带参数二维码
     *
     * @expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     * @param sceneId 场景Id
     * @return
     */
    public String createTempTicket(int sceneId) throws WxErrorException {
        String accessToken = wxMpService.getAccessToken();
        LOGGER.info("createTempTicket accessToken ={},sceneId={}",accessToken,sceneId);
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
        WechatQRCode wechatQRCode = null;
        TreeMap<String, String> params = new TreeMap<>();
        params.put("access_token", accessToken);
        Map<String, Integer> intMap = new HashMap<>();
        intMap.put("scene_id", sceneId);
        Map<String, Map<String, Integer>> mapMap = new HashMap<>();
        mapMap.put("scene", intMap);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("expire_seconds", 30*24*60*60);
        paramsMap.put("action_name","QR_SCENE");
        paramsMap.put("action_info", mapMap);
        Gson gson = new Gson();
        String data = gson.toJson(paramsMap);
        String res = HttpClient.post(url,data);
        LOGGER.info("createTempTicket res = {}",res);
        return res;
    }

//    @GetMapping("/callBack")
//    public String callBack1(HttpServletRequest request, HttpServletResponse response){
//        String TOKEN = "inno72";
//        String signature = request.getParameter("signature");
//        String echostr = request.getParameter("echostr");
//        return echostr;
//    }

//    @PostMapping("/callBack")
//    public String callBack2(HttpServletRequest request, HttpServletResponse response){
//
//        LOGGER.info("callBack invoked");
//        Gson g = new Gson();
//        LOGGER.info("callBack param = {}",g.toJson(request.getParameterMap()));
//        String html = "<html>\n" +
//                "<body>hh</body>\n" +
//                "</html>";
//        return html;
//    }

    @GetMapping("/index")
    public void callBack3(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/inded.html");
    }



}
