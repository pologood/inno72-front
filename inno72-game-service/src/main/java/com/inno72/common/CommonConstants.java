package com.inno72.common;

//import com.framelib.utils.ConfigUtils;
import org.springframework.beans.factory.BeanFactory;

/**
 * Common Description:静态变量列表
 * 
 * @Project : maxtp
 * @Program Name: com.framelib.common.Common.java
 * @ClassName : Common
 * @Author : zhangyan
 * @CreateDate : 2014-4-8 下午3:12:23
 */
public class CommonConstants {

	// 全局静态配置
	public static BeanFactory factory;

	public static int PageSize = 20;

	// todo 修改
	public static String IMAGE_URL = "";
	public static String SEND_MSG_TXT = "";
	public static String ZHIDAO_FEED_INFO_URL = "";
	public static String URI_CONTRACT_FINISH_DETAIL = "";
	public static String FAST_APPEND_QUESTION = "";
	public static String URI_CONTRACT_FINISH_DETAIL_APPID = "";
	public static String URL_17 = "";

	/**
	 * 服务电话
	 */
	public static final String SERVICE_PHONE = "400-960-999";
	
	/**
	 * 微信推送模板id静态类
	 * 
	 * @author zhouzengbao
	 *
	 */
	public static class WECHAT_MSG {
		/** 微信推送 - 创建合约单推送 id */
		public static final String WECHAT_MSG_FOR_CREATEO = "create_orders";
		/** 微信推送 - 新创建合约单推送 （新） 2017年09月28日*/
		public static final String WECHAT_MSG_FOR_CREATEO_NEW = "create_orders_new";
		/** 微信推送 - 取消合约单推送 id */
		public static final String WECHAT_MSG_FOR_CANCELO = "cancel_orders";
		/** 微信推送 - 取消合约单推送 （新） id 2017年10月09日*/
		public static final String WECHAT_MSG_FOR_CANCELO_NEW = "cancel_orders_new";
		/** 微信推送 - 更新合约单推送 id */
		public static final String WECHAT_MSG_FOR_UPDATEO = "update_orders";
		/** 微信推送 - 更新合约单推送 （新） id 2017年10月09日 */
		public static final String WECHAT_MSG_FOR_UPDATEO_NEW = "update_orders_new";
		/** 微信推送 - 完成合约单服务推送 id */
		public static final String WECHAT_MSG_FOR_FINISHO = "finish_orders";
		/** 微信推送 - 完成合约单状态推送 id */
		public static final String WECHAT_MSG_FOR_FINISH_STATUS = "finish_orders_status";
		/** 微信推送 - 完成门诊单状态推送 id 2017年11月14日 zhouzengbao*/
		public static final String WECHAT_MSG_OUTPATIENT_FOR_FINISH_STATUS = "finish_outpatient_orders_status";
		/** 微信推送 - 取消门诊订单状态推送 id 2017年11月24日 zhouzengbao */
		public static final String WECHAT_MSG_OUTPATIENT_FOR_CANCEL_STATUS = "cancel_outpatient_orders_status";
		/** 合约单完成 */
        public static final String WECHAT_MSG_FOR_CONTRACT_FINISH_STATUS = "finish_contract_orders_status";
    }
	
}
