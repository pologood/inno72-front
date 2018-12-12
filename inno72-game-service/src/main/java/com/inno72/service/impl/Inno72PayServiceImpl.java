package com.inno72.service.impl;

import com.inno72.common.Inno72BizException;
import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.Encrypt;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.utils.StringUtil;
import com.inno72.model.Inno72Order;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72PayService;
import com.inno72.service.Inno72QrCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class Inno72PayServiceImpl implements Inno72PayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72PayServiceImpl.class);
    @Autowired
    private Inno72GameServiceProperties properties;
    @Resource
    private Inno72QrCodeService qrCodeService;
    @Override
    public String pay(Inno72Order order) {
        //spid =1001
        Map<String,String> param = new HashMap<String,String>();
        param.put("notifyUrl",properties.get("notifyUrl"));
        param.put("outTradeNo",order.getId());
        param.put("qrTimeout",properties.get("qrTimeout"));
        param.put("quantity","1");
        param.put("spId",properties.get("spId"));
        param.put("subject","北京点七二创意互动传媒文化有限公司");
        param.put("terminalType",properties.get("terminalType"));
        BigDecimal temp = new BigDecimal(100);
        param.put("totalFee",order.getOrderPrice().multiply(temp).longValue()+"");
        param.put("transTimeout",properties.get("transTimeout"));
        param.put("type",order.getPayType()+"");
        param.put("unitPrice",order.getOrderPrice().multiply(temp).longValue()+"");
        String sign = genSign(param);
        param.put("sign",sign);
        String result = doInvoke(param);
        return manageResult(result);
//        PayParamVo vo = new PayParamVo();
//        vo.setNotifyUrl();
//        vo.setOutTradeNo();
//        vo.setQrTimeout(2);
//        vo.setQuantity(1);
//        vo.setSpId();
//        vo.setSubject();
//        vo.setTerminalType();
//        vo.setTotalFee();
//        vo.setTransTimeout();
//        vo.setType(order.getPayType());
//        vo.setUnitPrice(order.getOrderPrice());
//        return null;
    }

    private String manageResult(String respJson) {
        String code = FastJsonUtils.getString(respJson,"code");
        if(Result.SUCCESS == Integer.parseInt(code)){
            // TODO 新支付订单id到order
            String qrCode = FastJsonUtils.getString(respJson,"qrCode");
            //创建二维码
            String endUrl = StringUtil.getUUID()+".png";
            return qrCodeService.createQrCode(qrCode,endUrl);
        }else{
            LOGGER.error("支付异常",respJson);
            throw new Inno72BizException("支付异常");
        }
    }

    private String doInvoke(Map<String,String> param) {
        LOGGER.info("pay invoke param = {}",JsonUtil.toJson(param));
        String respJson = HttpClient.form(properties.get("payServiceUrl"), param, null);
        LOGGER.info("pay invoke response = {}",respJson);
        return respJson;
    }

    private String genSign(Map<String,String> param) {
        String paramStr =  createLinkString(param);
        String secureKey = properties.get("secureKey");
        String sign = paramStr + "&" + secureKey;
        sign = Encrypt.md5(sign);
        return sign;
    }

    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
}
