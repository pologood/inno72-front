package com.inno72.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;

public class SignUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUtil.class);

    public static String makeSign(Object obj, String secureKey) throws IllegalArgumentException, IllegalAccessException {

        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String, String> signMap = new HashMap<String, String>();
        for(Field field : fields){

            field.setAccessible(true);

            String type = field.getType().getSimpleName();
            String value = "";
            String key = field.getName();
            switch(type){
                case "Long":
                case "long":
                    value = field.get(obj) != null ? Long.toString((Long)field.get(obj)): null;
                    break;
                case "int":
                case "Integer":
                    value = field.get(obj) != null ? Long.toString((Integer)field.get(obj)): null;
                    break;
                case "String":
                    value = (String)field.get(obj);
                    break;
            }

            if(value != null) {
                signMap.put(key, value);
            }
        }

        signMap.remove("sign");

        String sign = createLinkString(signMap) + "&" + secureKey;

        LOGGER.info("perpare sign is " + sign);

        sign = Encrypt.md5(sign);

        return sign;

    }

	public static String genSign(Map<String,String> param, String secureKey) {
		String paramStr =  SignUtil.createLinkString(param);
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
