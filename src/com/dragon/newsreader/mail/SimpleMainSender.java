package com.dragon.newsreader.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SimpleMainSender {

	// 设置服务器
	private static String KEY_SMTP = "mail.smtp.host";
	private static String VALUE_SMTP = "smtp.qq.com";
	// 服务器验证
	private static String KEY_PROPS = "mail.smtp.auth";
	private static boolean VALUE_PROPS = true;
	// 发件人用户名、密码
	private String SEND_USER = "499360256@qq.com";
	private String SEND_UNAME = "499360256";
	private String SEND_PWD = "365zfl592++--";
	//收件人
	private static String RECEIVER_USER = "499360256@qq.com";
	// 建立会话
	private MimeMessage message;
	private Session s;

	public SimpleMainSender() {
		Properties props = System.getProperties();
		props.setProperty(KEY_SMTP, VALUE_SMTP);
		props.put(KEY_PROPS, VALUE_PROPS);
		s = Session.getInstance(props);
		message = new MimeMessage(s);
	}


	public boolean  doSendHtmlEmail(String headName, String sendHtml,
			String receiveUser) {
		boolean result = false;
		try {
			// 发件人
			InternetAddress from = new InternetAddress(SEND_USER);
			message.setFrom(from);
			// 收件人
			InternetAddress to = new InternetAddress(receiveUser);
			message.setRecipient(Message.RecipientType.TO, to);
			// 邮件标题
			message.setSubject(headName);
			String content = sendHtml.toString();
			// 邮件内容,也可以使纯文本"text/plain"
			message.setContent(content, "text/html;charset=utf-8");
			message.saveChanges();
			Transport transport = s.getTransport("smtp");
			// smtp验证，就是你用来发邮件的邮箱用户名密码
			transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
			// 发送
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			result = true;
			System.out.println("send success!");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	private static SimpleMainSender sSender ;
	
	public static boolean sendMail(String headName,String contentString){
		if(sSender == null)
			sSender = new SimpleMainSender();
		return sSender.doSendHtmlEmail(headName,contentString,RECEIVER_USER);
	}
	
}