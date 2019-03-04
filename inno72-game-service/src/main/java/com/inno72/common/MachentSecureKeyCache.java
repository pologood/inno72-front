package com.inno72.common;

import java.util.HashMap;
import java.util.Map;

public class MachentSecureKeyCache {
    private static Map<String,String> secureKeymap = new HashMap<String,String>();
    static{
        //贝德玛
        secureKeymap.put("100","qWoPcdVbdc");
    }
    public static String getSecureKey(String machentCode){
        return secureKeymap.get(machentCode);
    }
}
