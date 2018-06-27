//package com.inno72.common;
//
//import com.alibaba.fastjson.JSON;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.*;
//import javax.mail.internet.*;
//import java.io.File;
//import java.util.Date;
//import java.util.Enumeration;
//import java.util.Properties;
//import java.util.Vector;
//
//public class SendMailUtil {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(SendMailUtil.class);
//	// 设置服务器
//	private static String KEY_SMTP = "mail.smtp.host";
//	private static String VALUE_SMTP = "smtp.qq.com";
//	// 服务器验证
//	private static String KEY_PROPS = "mail.smtp.auth";
//	// 发件人用户名、密码
//	private static String SEND_USER = "3402679362@qq.com";
//	private String SEND_UNAME = "3402679362@qq.com";
//	private static String SEND_PWD = "rzkj131602";
//	// 建立会话
//	private MimeMessage message;
//	private Session s;
//
//	public static final int SEND_SUCCESS = 0;
//	public static final int SEND_FAILURE = 1;
//
//	/*
//	 * 初始化方法
//	 */
//	public SendMailUtil() {
//		Properties props = System.getProperties();
//		props.setProperty(KEY_SMTP, VALUE_SMTP);
//		props.put(KEY_PROPS, "true");
//		// props.put("mail.smtp.auth", "true");
//		s = Session.getDefaultInstance(props, new Authenticator() {
//			@Override
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(SEND_UNAME, SEND_PWD);
//			}
//		});
//		s.setDebug(true);
//		message = new MimeMessage(s);
//	}
//
//	/**
//	 * 发送邮件
//	 * 
//	 * @param headName
//	 *            邮件头文件名
//	 * @param sendHtml
//	 *            邮件内容
//	 * @param receiveUser
//	 *            收件人地址
//	 */
//	public int sendHtmlEmail(String headName, String sendHtml, String receiveUser) {
//		try {
//			// 发件人
//			InternetAddress from = new InternetAddress(SEND_USER);
//			message.setFrom(from);
//			// 收件人
//			InternetAddress to = new InternetAddress(receiveUser);
//			message.setRecipient(Message.RecipientType.TO, to);
//			// 邮件标题
//			message.setSubject(headName);
//			String content = sendHtml.toString();
//			// 邮件内容,也可以使纯文本"text/plain"
//			message.setContent(content, "text/html;charset=GBK");
//			message.saveChanges();
//			Transport transport = s.getTransport("smtp");
//			// smtp验证，就是你用来发邮件的邮箱用户名密码
//			transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
//			// 发送
//			transport.sendMessage(message, message.getAllRecipients());
//			transport.close();
//			return SendMailUtil.SEND_SUCCESS;
//		} catch (AddressException e) {
//			e.printStackTrace();
//			return SendMailUtil.SEND_FAILURE;
//		} catch (MessagingException e) {
//			e.printStackTrace();
//			return SendMailUtil.SEND_FAILURE;
//		}
//	}
//
//	public static boolean sendMail(String subject, String toMail,
//			String content, File... files) {
//		boolean isFlag = false;
//		try {
//
//			int port = 465; //端口
//
//			Properties props = new Properties();
//			props.put("mail.smtp.host", "smtp.qq.com");
//			props.put("mail.smtp.auth", "true");
//			props.put("mail.smtp.ssl.enable", "true");
//
//			LOGGER.info("开始发送邮件 {} {} {} {} {} {}",subject ,toMail ,content ,files ,port , JSON.toJSONString(props));
//			Session session = Session.getDefaultInstance(props);
//			session.setDebug(false);
//
//			MimeMessage message = new MimeMessage(session);
//			try {
//				message.setFrom(new InternetAddress("diansjsj@pinwheelmedical.com"));
//				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
//				message.setSubject(subject, "UTF-8");
//				message.addHeader("charset",  "UTF-8");
//
//				/*添加正文内容*/
//				Multipart multipart = new MimeMultipart();
//				BodyPart contentPart = new MimeBodyPart();
//				contentPart.setText(content);
//				contentPart.setContent(content, "text/html;charset=UTF-8");
//
//				multipart.addBodyPart(contentPart);
//
//				/*添加附件*/
//				for (File file : files) {
//					MimeBodyPart fileBody = new MimeBodyPart();
//					DataSource source = new FileDataSource(file);
//					fileBody.setDataHandler(new DataHandler(source));
//					fileBody.setFileName(file.getName());
//					multipart.addBodyPart(fileBody);
//				}
//
//				message.setContent(multipart);
//				message.setSentDate(new Date());
//				message.saveChanges();
//				Transport transport = session.getTransport("smtp");
//				transport.connect("smtp.exmail.qq.com", port, "diansjsj@pinwheelmedical.com", "Yyxk@2016");
//				transport.sendMessage(message, message.getAllRecipients());
//				transport.close();
//				isFlag = true;
//			} catch (Exception e) {
//				LOGGER.info("发送邮件异常 {} ,{}"+e.getMessage(),e);
//				isFlag = false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		LOGGER.info("发送邮件{}结束 ",isFlag ?"成功" :"失败");
//		return isFlag;
//	}
//
//
//	public static String toChinese(String text) {
//		try {
//			text = MimeUtility.encodeText(new String(text.getBytes(), "GB2312"), "GB2312", "B");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return text;
//	}
//
//	public static boolean sendMail(MailBean mb) {
//		String host = mb.getHost();
//		final String username = mb.getUsername();
//		final String password = mb.getPassword();
//		String from = mb.getFrom();
//		String to = mb.getTo();
//		String subject = mb.getSubject();
//		String content = mb.getContent();
//		String fileName = mb.getFilename();
//		Vector<String> file = mb.getFile();
//
//		Properties props = System.getProperties();
//		props.put("mail.smtp.host", host); // 设置SMTP的主机
//		props.put("mail.smtp.auth", "true"); // 需要经过验证
//
//		Session session = Session.getInstance(props, new Authenticator() {
//			public PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(username, password);
//			}
//		});
//
//		try {
//			MimeMessage msg = new MimeMessage(session);
//			msg.setFrom(new InternetAddress(from));
//			InternetAddress[] address = { new InternetAddress(to) };
//			msg.setRecipients(Message.RecipientType.TO, address);
//			msg.setSubject(subject,"UTF-8");
//
//			Multipart mp = new MimeMultipart();
//			MimeBodyPart mbpContent = new MimeBodyPart();
//			mbpContent.setText(content);
//			mp.addBodyPart(mbpContent);
//
//			/* 往邮件中添加附件 */
//			if (file != null) {
//				Enumeration<String> efile = file.elements();
//				while (efile.hasMoreElements()) {
//					MimeBodyPart mbpFile = new MimeBodyPart();
//					fileName = efile.nextElement().toString();
//					FileDataSource fds = new FileDataSource(fileName);
//					mbpFile.setDataHandler(new DataHandler(fds));
//					mbpFile.setFileName(toChinese(fds.getName()));
//					mp.addBodyPart(mbpFile);
//				}
//				System.out.println("添加成功");
//			}
//
//			msg.setContent(mp);
//			msg.setSentDate(new Date());
//			Transport.send(msg);
//
//		} catch (MessagingException me) {
//			me.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//}
