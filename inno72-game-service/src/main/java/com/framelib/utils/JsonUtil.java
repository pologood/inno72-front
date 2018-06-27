package com.framelib.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;


public class JsonUtil {

	public static String toJson(Object object) {
		return toJson(object, DateUtil.getDatePattern());

	}

	/**
	 * 
	 * @param json
	 *            json串
	 * @param clazz
	 *            生成的bean class
	 * @return 生成对象
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

	/**
	 * 
	 * @param json
	 *            json串
	 * @param clazz
	 *            生成的List的元素class
	 * @return List<T>
	 */
	public static <T> List<T> toArray(String json, Class<T> clazz) {

		return JSON.parseArray(json, clazz);
	}

	/**
	 * 
	 * @param object
	 *            对象
	 * @param dateFormat
	 *            日期字符串
	 * @return
	 */
	public static String toJson(Object object, String dateFormat) {
		return JSON.toJSONStringWithDateFormat(object, dateFormat, SerializerFeature.WriteDateUseDateFormat);

	}

}
