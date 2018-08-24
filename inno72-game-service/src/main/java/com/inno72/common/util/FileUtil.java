package com.inno72.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Encoder;

public class FileUtil {

	public static String toBase64(InputStream in, String startWith) {

		byte[] data = null;

		// 读取图片字节数组
		try {

			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();

		return startWith + encoder.encode(data);// 返回Base64编码过的字节数组字符串

	}

	public static boolean isExitsPath(String path) throws InterruptedException {
		String[] paths = path.split("/");
		StringBuilder fullPath = new StringBuilder();
		for (String name : paths) {
			if (name.contains(".zip")) {
				name = name.substring(0, name.indexOf("."));
			}
			fullPath.append(name).append("/");
			File file = new File(fullPath.toString());
			if (!file.exists()) {
				file.mkdir();
			}
		}
		File file = new File(fullPath.toString());//目录全路径
		return !file.exists();
	}
}
