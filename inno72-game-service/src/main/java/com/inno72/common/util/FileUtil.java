package com.inno72.common.util;

import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Encoder;

public class FileUtil {

	public static String toBase64(InputStream in, String startWith){

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

}
