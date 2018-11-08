/*
 * Copyright (c) 2017,www.levect.com. All rights reserved.
 *
 * 警告：本计算机程序受著作权法和国际公约的保护，未经授权擅自复制或散布本程序的部分或全部、以及其他
 * 任何侵害著作权人权益的行为，将承受严厉的民事和刑事处罚，对已知的违反者将给予法律范围内的全面制裁。
 *
 */

package com.inno72.common.util;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

/**
 * Description:集合操作工具类
 *
 * @author gaoxingang
 * @version 1.0.0
 */
/*
 * =========================== 维护日志 ===========================
 * 2015-01-28 09:28  gaoxingang 新建代码
 * =========================== 维护日志 ===========================
 */
public class CollectionUtil {

	private CollectionUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 判断集合是否非空
	 * @param collection
	 * @return
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return CollectionUtils.isNotEmpty(collection);
	}

	/**
	 * 判断集合是否为空
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return CollectionUtils.isEmpty(collection);
	}
}
