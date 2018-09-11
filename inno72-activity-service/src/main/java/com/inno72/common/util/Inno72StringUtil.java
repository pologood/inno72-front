package com.inno72.common.util;

public class Inno72StringUtil {
    /**
     * 生成四位随机数
     * @return
     */
    public static String genVerificationCode() {
        int num= (int)(Math.random()*9000+1000);
        return ""+num;
    }
}
