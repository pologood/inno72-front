package com.inno72.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class FileUtil {
	public static void main(String[] args) throws FileNotFoundException {
		InputStream is = new FileInputStream("/Users/72cy-0101-01-0023/Documents/ceshi.jpeg");
		System.out.println(toBase64(is,"base64"));
	}

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

	public static boolean isExitsPath(String path) throws InterruptedException{
		String [] paths=path.split("/");
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
		File file=new File(fullPath.toString());//目录全路径
		return !file.exists();
	}

	public static byte[] base64ToByte(String imgStr){
		if (imgStr == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		// 解密
		byte[] b = null;
		try {
			b = decoder.decodeBuffer(imgStr);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	public static boolean generateImage(String imgStr, String path) {
		if (imgStr == null) return false;
		BASE64Decoder decoder = new BASE64Decoder();
	    try {
			// 解密
			byte[] b = decoder.decodeBuffer(imgStr);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
	    	e.printStackTrace();
			return false;
		}
	}
}
