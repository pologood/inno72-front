package com.framelib.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.framelib.utils.DateUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class DataObj extends LinkedHashMap<String, Object> implements Serializable {
	private static final long serialVersionUID = -2091722852391095004L;

	public DataObj() {

	}

	public DataObj(Object obj) {
		this.fromObj(obj);
	}

	public void fromObj(Object obj) {
		try {
			Class<? extends Object> clazz = obj.getClass();
			for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					Object value = field.get(obj);
					if (value != null) {
						this.put(field.getName(), value);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return JSON.toJSONStringWithDateFormat(this, DateUtil.getDatePattern(),
				SerializerFeature.WriteDateUseDateFormat);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> t) {
		return (T) this.get(key);
	}

}
