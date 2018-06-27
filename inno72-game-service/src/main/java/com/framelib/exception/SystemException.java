package com.framelib.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SystemException继承了java.lang.RuntimeException，标志着该类并非强制后续开发人员来处理的异常
 * 
 * System更多的意味着框架的概念，而非传统的业务逻辑概念，所以本类更多是面向框架角度来封装异常行为
 * 
 * 
 * 类设计的初衷是为了满足以下两点需求： 与Struts的ActionMessage能够方便的整合
 * 
 * 能够全面的保留系统产生异常的所有信息，以便用户调试程序
 * 
 * 故此，我们在类中引入了两个常用的实例变量：
 * 
 * ActionMessage actionError Throwable throwable
 * 
 * 
 */
public class SystemException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4175077003917831650L;
	private static Logger log = LoggerFactory.getLogger(SystemException.class);
	/** 为能够全面的保留系统产生异常的所有信息，以便用户调试程序而引入的实例变量 */
	private Throwable throwable;
	private String msg;
	// 错误信息代码
	private String msgId;

	private static Map<String, String> errorInfoMap = new HashMap<String, String>();

	static {
		errorInfoMap.put("sessionOverdue", "未登录");
		errorInfoMap.put("permission.denied", "没有权限");
		errorInfoMap.put("400", "参数错误");
		errorInfoMap.put("405", "请求方式错误");
		errorInfoMap.put("500", "系统错误");
		errorInfoMap.put("415", "数据类型不支持");
		errorInfoMap.put("404", "请求未找到");
	}

	/**
	 * 为抛出的错误信息中传入参数
	 * 
	 * @param msgId
	 *            参数ID
	 * @param obj
	 *            替换文字集合
	 * @return 抛出的错误信息
	 */
	private static String makeError(String msgId, Object[] obj) {
		String msgValue = "";
		try {
			msgValue = (String) errorInfoMap.get(msgId);
			String[] values = msgValue.split("\\{");
			for (int i = 0; i < values.length; i++) {
				msgValue = msgValue.replace("{" + String.valueOf(i) + "}", obj.length - 1 < i ? "" : obj[i].toString());
			}
		} catch (Exception e) {
			log.error(e.toString());
		}
		return msgValue;
	}

	/**
	 * 为抛出的错误信息中传入参数
	 * 
	 * @Create_by:yinsy
	 * @Create_date:2013-8-16
	 * @param msgId
	 *            msgId 参数ID
	 * @return 抛出的错误信息
	 * @Last_Edit_By:
	 * @Edit_Description:
	 * @Create_Version:178xf 1.0
	 */
	private static String makeError(String msgId) {
		String msgValue = "";
		try {
			msgValue = (String) errorInfoMap.get(msgId);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return msgValue;
	}

	/**
	 * 构造函数
	 * 
	 * 
	 * @param msgId
	 *            String 资源文件中的信息编号
	 * @throws Exception
	 */
	public SystemException(String msgId) {
		// 调用父类的构造函数
		super(makeError(msgId));
		this.msg = super.getMessage();
		this.msgId = msgId;
	}

	/**
	 * 构造函数
	 * 
	 * 
	 * @param msgId
	 *            String 资源文件中的信息编号
	 * @param arguments
	 *            Object[] 资源文件中的信息所需的额外参数
	 * @throws Exception
	 * 
	 */
	public SystemException(String msgId, Object arguments[]) {
		// 调用父类的构造函数
		super(makeError(msgId, arguments));
		this.msgId = msgId;
	}

	/**
	 * 构造函数
	 * 
	 * 
	 * @param msgId
	 *            String 资源文件中的信息编号
	 * @param throwable
	 *            Throwable 抛出的异常信息
	 * 
	 */
	public SystemException(String msgId, Throwable throwable) {
		// 调用父类的构造函数

		super(makeError(msgId));
		this.throwable = throwable;
		this.msgId = msgId;
	}

	/**
	 * 构造函数
	 * 
	 * 根据输入参数，初始化类实例变量
	 * 
	 * 
	 * @param msgId
	 *            String 资源文件中的信息编号
	 * @param arguments
	 *            Object[] 资源文件中的信息所需的额外参数
	 * 
	 * @param throwable
	 *            Throwable 抛出的异常信息
	 * 
	 */
	public SystemException(String msgId, Object arguments[], Throwable throwable) {
		super(makeError(msgId, arguments));
		this.throwable = throwable;
		this.msgId = msgId;

	}

	/**
	 * 返回异常对象
	 * 
	 * @return Throwable 异常对象
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * 设置异常对象
	 * 
	 * @param throwable
	 *            Throwable 异常对象
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public SystemException(String msgId, String msg) {
		this.msgId = msgId;
		this.msg = msg;
	}

	@Override
	public String getMessage() {
		return msg;
	}
}
