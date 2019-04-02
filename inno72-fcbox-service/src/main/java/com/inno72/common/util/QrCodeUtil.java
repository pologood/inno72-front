package com.inno72.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

/**
 * 二维码生成和读的工具类
 *
 */
public class QrCodeUtil {
	/**
	 * 生成包含字符串信息的二维码图片
	 * @param outputStream 文件输出流路径
	 * @param content 二维码携带信息
	 * @param qrCodeSize 二维码图片大小
	 * @param imageFormat 二维码的格式
	 * @throws WriterException
	 * @throws IOException
	 */
	public static boolean createQrCode(String filePath, String content, int qrCodeSize, String imageFormat)
			throws WriterException, IOException {
		//设置二维码纠错级别ＭＡＰ
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  // 矫错级别
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		//创建比特矩阵(位矩阵)的QR码编码的字符串
		BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
		// 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth - 200, matrixWidth - 200, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// 使用比特矩阵画并保存图像
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i - 100, j - 100, 1, 1);
				}
			}
		}
		OutputStream outputStream = new FileOutputStream(new File(filePath));
		return ImageIO.write(image, imageFormat, outputStream);
	}

	/**
	 * 读二维码并输出携带的信息
	 */
	public static String readQrCode(InputStream inputStream) throws IOException {
		//从输入流中获取字符串信息
		BufferedImage image = ImageIO.read(inputStream);
		//将图像转换为二进制位图源
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		Result result = null;
		try {
			result = reader.decode(bitmap);
		} catch (ReaderException e) {
			e.printStackTrace();
		}
		return result.getText();
	}

	/**
	 * 读二维码并输出携带的信息
	 */
	public static String readQrCode(String url) throws IOException {
		try (InputStream is = parseUrl(url)) {
			String result = readQrCode(is);
			return result;
		}
	}

	public static InputStream parseUrl(String path) throws IOException {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
		conn.setDoInput(true);
		conn.connect();
		InputStream is = conn.getInputStream();    //得到网络返回的输入流
		return is;
	}


	/**
	 * 生成讲课二维码
	 * @param content
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	public static String createTeachQRCode(String id) throws WriterException, IOException {
		String qrCodeUrl = "/qrcode/" + id + ".jpg";
		createQrCode("src\\main\\webapp\\qrcode\\" + id + ".jpg", id, 900, "JPEG");
		return qrCodeUrl;
	}

	/**
	 * 测试代码
	 * @throws WriterException
	 */
	//    public static void main(String[] args) throws IOException, WriterException {
	//        //createQrCode(new FileOutputStream(new File("d:\\qrcode.jpg")),"china is good",900,"JPEG");
	//    	String url = "gaohuan";
	//    	createQrCode("src\\main\\webapp\\qrcode\\qrcode.jpg",url,10000,"JPEG");
	//        readQrCode(new FileInputStream(new File("src\\main\\webapp\\qrcode\\qrcode.jpg")));
	//    }


}