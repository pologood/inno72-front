//package com.framelib.utils;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.aliyun.openservices.ons.api.Message;
//import com.aliyun.openservices.ons.api.Producer;
//import com.gexin.rp.sdk.base.IPushResult;
//import com.gexin.rp.sdk.base.impl.SingleMessage;
//import com.gexin.rp.sdk.base.impl.Target;
//import com.gexin.rp.sdk.base.payload.APNPayload;
//import com.gexin.rp.sdk.exceptions.RequestException;
//import com.gexin.rp.sdk.http.IGtPush;
//import com.gexin.rp.sdk.template.NotificationTemplate;
//import com.gexin.rp.sdk.template.TransmissionTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.annotation.Resource;
//import java.io.UnsupportedEncodingException;
//
///**
// * ios推送 TODO 改为消息中心发送
// */
//public class IOSGPushUtil {
//
//	private Logger logger = LoggerFactory.getLogger(GPushUtil.class);
//
//	@Resource(name = "pushMsgProducer")
//	private Producer producer;
//
//	private static String topic = String.valueOf("push_business_test");
//
//	private static String appKey = "jez1tDqbEe9phz2yF36JG7";
//	private static String masterSecret = "Jfwi4nOPT07rcRFwgjFi35";
//	static String host = "http://sdk.open.api.igexin.com/apiex.htm";
//
//	public void pushMessage(String json, String registrationId) {
//
//		JSONObject _json = JSON.parseObject(json);
//		_json.put("$registrationId", registrationId);
//		_json.put("$method", "single");
//
//		logger.info("pushMessage ({} )", _json);
//
//		String msg = _json.toJSONString();
//		Message message = null;
//		try {
//			message = new Message(topic, "pushMsg", msg.getBytes("UTF-8"));
//			message.setKey("pushMsg");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		producer.sendOneway(message);
//
//	}
//
//	public void pushNotify(String text, String registrationId) {
//
//		logger.info("pushNotify ({})", text);
//
//		JSONObject _json = new JSONObject();
//		_json.put("$method", "publish");
//
//		_json.put("$registrationId", registrationId);
//		_json.put("msg", text);
//
//		String msg = _json.toJSONString();
//		Message message = null;
//		try {
//			message = new Message(topic, "pushNotify", msg.getBytes("UTF-8"));
//			message.setKey("pushNotify");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		producer.sendOneway(message);
//
//	}
//
//	private static IGtPush gtPush;
//
//	static {
//		gtPush = new IGtPush(host, appKey, masterSecret);
//	}
//
//	public IPushResult getSendResult(String taskId) {
//		IPushResult result = gtPush.getPushResult(taskId);
//		return result;
//	}
//
//	// ---------------------------------风车医生---------------------------------
//	// 采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
//	private static String _appId = "0sruqEedAF8uLdnH2y8Qq6";
//	private static String _appKey = "yJzomFPMUH8SPA5Oku1fG4";
//	private static String _masterSecret = "ml16ns9lOQ7NHIwbjKewj3";
//	static String _host = "http://sdk.open.api.igexin.com/apiex.htm";
//	private static IGtPush _gtPush;
//
//	static {
//		_gtPush = new IGtPush(_host, _appKey, _masterSecret);
//	}
//
//	/**
//	 * pushTransmissionTemplate by alias
//	 *
//	 * @Create_by:WangShaoPeng
//	 * @Create_date:2017年3月6日
//	 * @param:
//	 * @return:
//	 * @Last_Edit_By:WangShaoPeng
//	 * @Edit_Description:
//	 * @Create_Version:
//	 */
//	public IPushResult pushTransmissionTemplate(String text, String json, String alias) {
//		TransmissionTemplate _template = transmissionTemplateDemo(json);
//		logger.info("$$$透传模板内容-{}", _template);
//
//		SingleMessage message = new SingleMessage();
//		message.setOffline(true);
//		// 离线有效时间，单位为毫秒，可选
//		message.setOfflineExpireTime(24 * 3600 * 1000);
//		message.setData(_template);
//		// 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
//		message.setPushNetWorkType(0);
//
//		APNPayload payload = new APNPayload();
//		// 在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
//		payload.setAutoBadge("+1");
//		payload.setContentAvailable(1);
//		payload.setSound("default");
//		payload.setAlertMsg(new APNPayload.SimpleAlertMsg(text));
//		_template.setAPNInfo(payload);
//
//		Target target = new Target();
//		target.setAppId(_appId);
//		target.setAlias(alias);
//		IPushResult ret = null;
//		try {
//			ret = _gtPush.pushMessageToSingle(message, target);
//			logger.info("push-app-正常推送分配医生-{}", ret.getResponse().toString());
//		} catch (RequestException e) {
//			// e.printStackTrace();
//			logger.info("eeeeee-分配解读医生异常", e);
//			ret = _gtPush.pushMessageToSingle(message, target, e.getRequestId());
//			logger.info("push-app-异常推送分配医生-{}", ret.getResponse().toString());
//		}
//		return ret;
//	}
//
//	public IPushResult pushNotificationTemplate(String title, String text, String alias) {
//		NotificationTemplate template = new NotificationTemplate();
//
//		template.setAppId(_appId);
//		template.setAppkey(_appKey);
//		template.setTitle(title);
//		template.setText(text);
//		SingleMessage message = new SingleMessage();
//		message.setOffline(true);
//		// 离线有效时间，单位为毫秒，可选
//		message.setOfflineExpireTime(24 * 3600 * 1000);
//		message.setData(template);
//		// 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
//		message.setPushNetWorkType(0);
//		Target target = new Target();
//		target.setAppId(_appId);
//		// target.setClientId(CID);
//		target.setAlias(alias);
//		IPushResult ret = null;
//		try {
//			ret = _gtPush.pushMessageToSingle(message, target);
//		} catch (RequestException e) {
//			e.printStackTrace();
//			ret = _gtPush.pushMessageToSingle(message, target, e.getRequestId());
//		}
//		return ret;
//	}
//
//	/**
//	 * 创建TransmissionTemplate模板
//	 *
//	 * @Create_by:WangShaoPeng
//	 * @Create_date:2017年3月6日
//	 * @param:
//	 * @return:
//	 * @Last_Edit_By:WangShaoPeng
//	 * @Edit_Description:
//	 * @Create_Version:
//	 */
//	static TransmissionTemplate transmissionTemplateDemo(String json) {
//		TransmissionTemplate template = new TransmissionTemplate();
//		template.setAppId(_appId);
//		template.setAppkey(_appKey);
//		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
//		template.setTransmissionType(2);
//		template.setTransmissionContent(json);
//		// 设置定时展示时间
//		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
//		return template;
//	}
//}
