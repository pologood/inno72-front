package com.framelib.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//import com.framelib.utils.ConfigUtils;

/**
 * Title:CommonConstants Description:基础数据类
 *
 * @Create_by:yinsy
 * @Create_date:2016年3月24日
 * @Last_Edit_By:
 * @Edit_Description:
 * @version:hma-framelib 1.0
 */
public class CommonConstants {

	public final static Properties SYSTEM_PROPS = null;

	// 系统环境，生产环境
	public static final String ENVI_PROD = "prod";

	// 系统环境，测试环境
	public static final String ENVI_TEST = "test";

	// 系统环境，开发环境
	public static final String ENVI_DEV = "dev";

	// 客户头像的路径
	public final static String CUSTOMER_AVATAR_FILE_PATH = "avatar";

	// 名片的路径
	public final static String BUSICARD_FILE_PATH = "card";

	/**
	 * 头像 名片后缀类型
	 */
	public static final String IMG_SUFFIX_TYPE = ".jpg";

	// 头像尺寸
	public final static Map<String, String> IMAGE_CUSTOMER_AVATAR_SIZE_MAP = new HashMap<String, String>();

	// 名片尺寸
	public final static Map<String, String> IMAGE_BUSICARD_SIZE_MAP = new HashMap<String, String>();

	public final static int ONE_DAY_USERSYM_EXP = 24 * 3600;

	static {
		// key:头像尺寸，value:width*height，width为0则为定高缩放，height为0则定高缩放
		IMAGE_CUSTOMER_AVATAR_SIZE_MAP.put("400400", "400*400");
		IMAGE_CUSTOMER_AVATAR_SIZE_MAP.put("200200", "200*200");
		IMAGE_CUSTOMER_AVATAR_SIZE_MAP.put("150150", "150*150");
		IMAGE_CUSTOMER_AVATAR_SIZE_MAP.put("100100", "100*100");
		IMAGE_CUSTOMER_AVATAR_SIZE_MAP.put("8080", "80*80");
		IMAGE_CUSTOMER_AVATAR_SIZE_MAP.put("5050", "50*50");
		IMAGE_CUSTOMER_AVATAR_SIZE_MAP.put("3030", "30*30");

		// 名片尺寸
		IMAGE_BUSICARD_SIZE_MAP.put("400200", "0*200");
	}


	// // 登录时创建cookie的域
	// public static final String LOGIN_COOKIE_DOMAIN = (String)
	// COOKIE_PROPS.get("login.cookie.domain");
	//
	// // 登录时创建cookie的名称
	// public static final String LOGIN_COOKIE_NAME = (String)
	// COOKIE_PROPS.get("login.cookie.name");
	//
	// // 登录时创建cookie的有效时间（秒）；-1：即不设置有效时间，关闭浏览器后cookie自动失效
	// public static final String LOGIN_COOKIE_MAXAGE = (String)
	// COOKIE_PROPS.get("login.cookie.maxage");

	// // 登录时创建cookie的域
	// public static final String MAIL_SERVER = (String)
	// MAIL_PROPS.get("mail.server");
	//
	//
	// public static final String MAIL_LINK = (String)
	// MAIL_PROPS.get("mail.link");

	// 后台用户登录session KEY
	public final static String USER_SESSION = "sessionId";

	// 验证码
	public final static String VALIDATE_CODE_KEY = "_validateCode";

	// 登陆验证码
	public final static String VALIDATE_LOGIN_CODE_KEY = "loginValidateCode";

	// 记住账号的checkbox的name属性
	public final static String CHECKBOX_REMEMBER_USERNAME = "checkbox_phone";

	// 记住密码的checkbox的name属性
	public final static String CHECKBOX_AUTO_LOGIN = "checkbox_autoLogin";

	// 自动登陆在cookie中的标志
	public final static String COOKIE_AUTOLOGIN_MARK = "mark";

	// 自动登陆标志设为String "1"
	public final static String AUTOLOGIN_YES = "1";

	// 不自动登陆标志设为String "0"
	public final static String AUTOLOGIN_NO = "0";

	// 存入cookie中的路径
	public final static String COOKIE_PATH = "/";

	// 存入cookie中的账号名称
	public final static String COOKIE_USERNAME = "username";

	// cookie有效时间
	public final static int COOKIE_MAXAGE = 60 * 60 * 24 * 7;

	// 操作存入Session中的UUID的key
	public static final String SESSION_UPDATE_PASSWORD_KEY = "uuid_key";

	/**
	 * 发送邮件内容模版开始 // 发送邮件内容 demo 模版 public static final String
	 * MAIL_DEMO_TEMPLATE = (String) TEMPLATE_PROPS.get("demo.path");
	 *
	 * // 发送邮件修改密码链接 模版 public static final String MAIL_UPDATE_PASSWORD_TEMPLATE
	 * = (String) TEMPLATE_PROPS.get("updatePassword.path");
	 *
	 * //发送邮件修改密码 主题 public static final String MAIL_UPDATE_PASSWORD_SUBJECT =
	 * (String) TEMPLATE_PROPS.get("updatePassword.subject");
	 *
	 * //发送验证码的邮件 模版 public static final String MAIL_VALIDATE_CODE_TEMPLATE =
	 * (String) TEMPLATE_PROPS.get("sendValidate.path");
	 *
	 * //改善邮件验证码验证身份 主题 public static final String MAIL_VALIDATE_CODE_SUBJECT =
	 * (String) TEMPLATE_PROPS.get("sendValidate.subject");
	 *
	 * //修改密保邮箱的邮件 模版 public static final String MAIL_UPDATE_EMAIL_TEMPLATE =
	 * (String) TEMPLATE_PROPS.get("updateEmail.path");
	 *
	 * //修改密保邮箱的邮件 主题 public static final String MAIL_UPDATE_EMAIL_SUBJECT =
	 * (String) TEMPLATE_PROPS.get("updateEmail.subject");
	 */
	// 客户类型
	public final static String CLIENT_TYPE = "clientType";

	// IOS
	public final static String CLIENT_TYPE_IOS = "IOS";

	// Android
	public final static String CLIENT_TYPE_ANDROID = "Android";

	/** 发送邮件内容模版结束 */

	/** 短信验证码key **/
	public static final String SMS_CODE_KEY = "SMSCodeKey";

	// 操作标示 成功
	public final static int RETURN_CODE_SUCCESS = 1;

	// 操作标示 失败
	public final static int RETURN_CODE_FAIL = 0;

	public final static String CURR_IMG_PATH_FLAG = "<img-path>";

	public static PmpSessionData PMP_SESSION_DATA = new PmpSessionData();

	public final static int PMP_SESSION_DATA_EXP = 3600 * 24;

	public enum Del {
		YES(1), NO(0);
		private int v;

		private Del(int v) {
			this.v = v;
		}

		public int v() {
			return this.v;
		}
	}

	public enum MEMCACHED_KEY {
		// 公众号token
		TOKEN("$TOKEN"),
		// 公众号ticket
		TICKET("$TICKET"),
		// 企业号token
		QY_TOKEN("$QY_TOKEN"),
		// 企业号ticket
		QY_TICKET("$QY_TICKET"),
		// 钉钉token
		DD_TOKEN("$DD_TOKEN"),
		// 钉钉ticket
		DD_TICKET("$DD_TICKET"),
		// 钉钉扫码登录token
		DD_LOGIN_TOKEN("$DD_LOGIN_TOKEN");

		private String v;

		private MEMCACHED_KEY(String v) {
			this.v = v;
		}

		public String v() {
			return this.v;
		}
	}

	// **********************************************七陌**********************************************
	// 异步呼叫
	public final static String WEB_CALL_ASYNCHRONOUS = "http://114.215.198.159/command?Action=Webcall&Account=N00000007765&PBX=bj.ali.6.5&ServiceNo=01011111373&WebCallType=asynchronous&CallBackType=post&Timeout=60&Exten={0}&Variable=phoneNum:{1}&CallBackUrl={2}&ActionID={3}";
	// 同步呼叫
	public final static String WEB_CALL_SYNCHRONIZATION = "http://114.215.198.159/command?Action=Webcall&Account=N00000007765&PBX=bj.ali.6.5&ServiceNo=01011111373&CallBackType=post&Timeout=60&Exten={0}&Variable=phoneNum:{1}&CallBackUrl={2}&ActionID={3}";
	// **********************************************七陌**********************************************

	public enum SEX {
		/**
		 * 男
		 */
		MALE(1),

		/**
		 * 女
		 */
		FMALE(0);

		private int v;

		private SEX(int v) {
			this.v = v;
		}

		public int v() {
			return this.v;
		}
	}

	/**
	 * 编码格式
	 *
	 * @author gavin
	 *
	 */
	public enum ENCODE {
		UTF8("UTF-8"), GBK("GBK"), IOS88591("ISO8859-1");

		private String v;

		private ENCODE(String v) {
			this.v = v;
		}

		public String v() {
			return this.v;
		}
	}

}
