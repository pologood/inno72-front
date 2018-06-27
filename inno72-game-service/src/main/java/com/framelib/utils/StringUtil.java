package com.framelib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串处理工具类
 * 
 * @Project : maxtp.framelib
 * @Program Name: com.framelib.utils.StringUtil.java
 * @ClassName : StringUtil
 * @Author : caozhifei
 * @CreateDate : 2014年4月25日 上午10:54:05
 */
public class StringUtil extends StringUtils {
	/**
	 * 将传入字符中的指定位置字符替换为新的字符 newChar
	 * 
	 * @Method_Name : replace
	 * @param str
	 *            原字符串
	 * @param start
	 *            替换开始位置
	 * @param end
	 *            替换截止位置
	 * @param newChar
	 *            替换的目的字符
	 * @return String 替换之后的字符串
	 * @return : String
	 * @Creation Date : 2014年4月16日 下午3:48:24
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	public static String replace(String str, int start, int end, char newChar) {
		if (str == null) {
			return null;
		}
		if (str == "") {
			return "";
		}
		int len = str.length();
		StringBuffer sb = new StringBuffer();
		if (end > len || start < 0 || start > end) {
			return "";
		}
		for (int i = 0; i < len; i++) {
			if (i >= start && i < end) {
				sb.append(newChar);
			} else {
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 把手机号码的第3位到第8位替换成*
	 * 
	 * @Method_Name : replacePhone
	 * @param mobile
	 * @return
	 * @return : String
	 * @Creation Date : 2014-4-16 下午4:06:53
	 * @version : v1.00
	 * @Author : zhangyan
	 * @Update Date :
	 * @Update Author :
	 */
	public static String replaceMobile(String mobile) {
		return replace(mobile, 3, 8, '*');
	}

	/**
	 * 将身份证号码的第4位到第16位替换成* ，身份证号必须18位
	 * 
	 * @Method_Name : replaceIdCard
	 * @param idCard
	 * @return
	 * @return : String
	 * @Creation Date : 2014年4月16日 下午4:17:33
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	public static String replaceIdCard(String idCard) {
		return replace(idCard, 4, 16, '*');
	}

	/**
	 * 将邮箱@之前的字符串首尾之间的字符替换为*，如536455458@qq.com 替换后为 5*****8@qq.com
	 * 
	 * @Method_Name : replaceEamil
	 * @param email
	 * @return
	 * @return : String
	 * @Creation Date : 2014年4月16日 下午4:35:01
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	public static String replaceEamil(String email) {
		if (!email.contains("@")) {
			return email;
		}
		String str = email.substring(0, email.indexOf("@"));
		StringBuffer sb = new StringBuffer();
		sb.append(str.charAt(0));
		sb.append("*****");
		sb.append(str.charAt(str.length() - 1));
		sb.append(email.substring(email.indexOf("@"), email.length()));
		return sb.toString();
	}

	/**
	 * 字符串为空
	 * 
	 * @Create_by:yinsy
	 * @Create_date:2014-4-29
	 * @param src
	 * @return
	 * @Last_Edit_By:
	 * @Edit_Description:
	 * @Create_Version:maxtp.framelib 1.0
	 */
	public static boolean isEmpty(Object src) {
		if (src == null || "".equals(src.toString().trim()) || "null".equals(src)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 字符串不为空
	 * 
	 * @Create_by:yinsy
	 * @Create_date:2014-4-29
	 * @param src
	 * @return
	 * @Last_Edit_By:
	 * @Edit_Description:
	 * @Create_Version:maxtp.framelib 1.0
	 */
	public static boolean notEmpty(Object src) {
		if (src == null || "".equals(src.toString().trim()) || "null".equals(src)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 数组转换成字符串 “,”号隔开
	 * 
	 * @Create_by:yinsy
	 * @Create_date:2014-4-29
	 * @param str
	 * @return
	 * @Last_Edit_By:
	 * @Edit_Description:
	 * @Create_Version:maxtp.framelib 1.0
	 */
	public static String convString(String[] str) {
		StringBuilder sb = new StringBuilder();
		if (str != null && str.length > 0) {
			for (int i = 0; i < str.length; i++) {
				sb.append(str[i]).append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 逗号隔开的数字字符串，转成long类型集合
	 * 
	 * @Method_Name : String2LongList
	 * @param str
	 * @return
	 * @return : List<Long>
	 * @Creation Date : 2014年4月29日 下午5:41:07
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	public static List<Long> String2LongList(String str) {
		List<Long> list = new ArrayList<Long>();
		if (StringUtils.isBlank(str)) {
			return list;
		} else {
			// 去掉最后一个空格
			if (str.lastIndexOf(",") == str.length()) {
				str = str.substring(0, str.length() - 1);
			}
			String[] arr = str.split(",");
			for (String s : arr) {
				list.add(Long.valueOf(s));
			}
			return list;
		}
	}

	/**
	 * UUID
	 * 
	 * @Create_by: Houkemian
	 * @Create_date:2016年11月15日
	 * @param
	 * @Last_Edit_By:
	 * @Edit_Description:
	 * @Create_Version:PMP
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 加密医生推荐医生id
	 * 
	 * @Create_by:lizh
	 * @Create_date:2017年2月9日下午4:35:55
	 * @param doctorId
	 * @return
	 * @Last_Edit_By:
	 * @Edit_Description:
	 * @Create_Version:framelib
	 */
	public static String encryDoctorRecomeDoctorId(String doctorId) {
		if (!StringUtil.isBlank(doctorId)) {
			if (doctorId.length() <= 3){

				if (doctorId.length() == 1) {
					doctorId = "00" + doctorId;
				} else if (doctorId.length() == 2) {
					doctorId = "0" + doctorId;
				}
				int f = Integer.parseInt(doctorId.substring(0, 1));
				int c = Integer.parseInt(doctorId.substring(1, 2));
				int t = Integer.parseInt(doctorId.substring(2, 3));

				StringBuffer sb = new StringBuffer();
				Random random = new Random();

				int ff = random.nextInt(10 - f);
				int fc = random.nextInt(10 - c);
				int ft = random.nextInt(10 - t);

				sb.append(c + fc).append(ft).append(f + ff).append(ff).append(t + ft).append(fc);
				if (sb.toString().length() != 6) {
					return null;
				}
				return sb.toString();
			}else if(doctorId.length() == 4){
				
				String substring4_1 = doctorId.substring(0,1);
				String substring6_2 = doctorId.substring(1,2);
				String substring1_3 = doctorId.substring(2,3);
				String substring3_4 = doctorId.substring(3,4);
				
				String substring2 = "2";
				String substring5 = "9";
				
				return substring1_3+substring2+substring3_4+substring4_1+substring5+substring6_2;
				
			}
		}
		return null;
	}
	

	/**
	 * 解密医生推荐医生id
	 * 
	 * @Create_by:lizh
	 * @Create_date:2017年2月9日下午4:36:19
	 * @param doctorId
	 * @return
	 * @Last_Edit_By:
	 * @Edit_Description:
	 * @Create_Version:framelib
	 */
	public static String decryDoctorRecomeDoctorId(String doctorId) {
		if (!StringUtil.isBlank(doctorId) && doctorId.length() == 6) {
			try {
				int a1 = Integer.parseInt(doctorId.substring(0, 1));
				int a2 = Integer.parseInt(doctorId.substring(1, 2));
				int a3 = Integer.parseInt(doctorId.substring(2, 3));
				int a4 = Integer.parseInt(doctorId.substring(3, 4));
				int a5 = Integer.parseInt(doctorId.substring(4, 5));
				int a6 = Integer.parseInt(doctorId.substring(5, 6));
				
				StringBuffer sb = new StringBuffer();
				if (a2 == 2 && a5 == 9){
					return sb.append(a4).append(a6).append(a1).append(a3).toString();
				}
				

				int s1 = a3 - a4;
				int s2 = a1 - a6;
				int s3 = a5 - a2;
				if (s1 < 0 || s2 < 0 || s3 < 0) {
					return null;
				}
				sb.append(s1).append(s2).append(s3);
				int result = Integer.parseInt(sb.toString());
				return String.valueOf(result);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
}
