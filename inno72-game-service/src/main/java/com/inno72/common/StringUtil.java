package com.inno72.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StringUtil {

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String getCardNum() {
		String result = "0";
		result += System.currentTimeMillis();
		return result;
	}

	public static String getUnscrambleOrderNum(){
		String time = new Date().getTime() + "";
		String orderNum = "32" + time.substring(time.length()-7,time.length()-2);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		orderNum += (date.substring(2,date.length()-2) + 20
				+ date.substring(date.length()-2,date.length())
				+ time.substring(time.length()-2,time.length()));
		return orderNum;
	}

}
