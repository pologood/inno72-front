package com.inno72.msg.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class MsgHandler {

	private Document doc;
	private Element root;

	public MsgHandler(String xml) {
		try {
			doc = DocumentHelper.parseText(xml);
			root = doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public String toXml(String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml><ToUserName><![CDATA[").append(getFromuser()).append("]]></ToUserName><FromUserName><![CDATA[")
				.append(getTouser()).append("]]></FromUserName><CreateTime>").append(System.currentTimeMillis())
				.append("</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[").append(msg)
				.append("]]></Content><MsgId>").append("123").append("</MsgId><AgentID>").append(getAgentId())
				.append("</AgentID></xml>");
		return sb.toString();

	}

	public String getNode(String node) {
		String result = root.elementText(node);
		return result;
	}

	public String getMsgType() {

		String msgType = root.elementText("MsgType");

		return msgType;
	}

	public String getAgentId() {

		String agentID = root.elementText("AgentID");

		return agentID;
	}

	public String getContent() {

		String content = root.elementText("Content");

		return content;
	}

	public String getEvent() {

		String event = root.elementText("Event");

		return event;
	}

	public String getFromuser() {
		String from = root.elementText("FromUserName");

		return from;
	}

	public String getTouser() {
		String to = root.elementText("ToUserName");
		return to;
	}

}
