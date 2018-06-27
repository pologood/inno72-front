package com.framelib.utils;

import java.util.HashMap;
import java.util.Map;

public class MsgTemplate {
	private String touser;
	private String template_id;
	private String url;
	private Map<String, DataItem> data;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, DataItem> getData() {
		return data;
	}

	public void setData(Map<String, DataItem> data) {
		this.data = data;
	}

	public void addData(String key, String value) {
		DataItem di = new DataItem();
		di.setValue(value);
		di.setColor("#173177");
		if (this.data == null) {
			this.data = new HashMap<String, DataItem>();
		}
		this.data.put(key, di);
	}

	public void addData(String key, String value, String color) {
		DataItem di = new DataItem();
		di.setValue(value);
		di.setColor(color);
		if (this.data == null) {
			this.data = new HashMap<String, DataItem>();
		}
		this.data.put(key, di);
	}

	class DataItem {
		private String value;
		private String color;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

	}

}
