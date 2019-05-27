/*
 * Created on 2003-5-16
 * 
 */
package cn.csdb.commons.util;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author bluejoe
 * 
 * ��������ʵ���ʼ��ķ��͡�
 * 
 */

public class Mail
{
	// �����ռ��ˡ������ˡ������
	private String description = "Content-Disposition";

	private String charset = "gb2312";

	private MimeMessage mm;

	private MimeBodyPart bodyPart = new MimeBodyPart();

	private Date sendDate;

	private List files = new Vector();

	/**
	 * @param smtpServer
	 *            SMTP��������ַ
	 */
	public Mail(String smtpServer)
	{
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpServer);

		Session session = Session.getDefaultInstance(props);
		mm = new MimeMessage(session);
	}

	/**
	 * @param smtpServer
	 * @param account
	 *            SMTP��֤�ʺ�
	 * @param password
	 *            SMTP��֤����
	 */
	public Mail(String smtpServer, String account, String password)
	{
		Properties props = java.lang.System.getProperties();
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.auth", "true");

		Authenticator sa = new SmtpAuthenticator(account, password);
		Session session = Session.getInstance(props, sa);
		mm = new MimeMessage(session);
	}

	/**
	 * ���÷�����
	 * 
	 * @param from
	 * @throws Exception
	 */
	public void setFrom(String from) throws Exception
	{
		mm.setFrom(new InternetAddress(from));
	}

	/**
	 * �����ռ���
	 * 
	 * @param to
	 * @throws Exception
	 */
	public void setRecipient(String to) throws Exception
	{
		InternetAddress[] address = { new InternetAddress(to) };
		mm.setRecipients(Message.RecipientType.TO, address);
	}

	/**
	 * ׷���ռ���
	 * 
	 * @param to
	 * @throws Exception
	 */
	public void addRecipient(String to) throws Exception
	{
		InternetAddress address = new InternetAddress(to);
		mm.addRecipient(Message.RecipientType.TO, address);
	}

	/**
	 * ���÷���ʱ��
	 * 
	 * @param sendDate
	 * @throws Exception
	 */
	public void setSendDate(Date sendDate) throws Exception
	{
		this.sendDate = sendDate;
	}

	/**
	 * ��������
	 * 
	 * @param subject
	 * @throws Exception
	 */
	public void setSubject(String subject) throws Exception
	{
		mm.setSubject(subject);
	}

	/**
	 * ׷���ļ�
	 * 
	 * @param filename
	 */
	public void addFile(String filename)
	{
		files.add(filename);
	}

	/**
	 * ������������
	 * 
	 * @param body
	 * @throws MessagingException
	 */
	public void setBody(String body) throws MessagingException
	{
		bodyPart.setText(body);
	}

	public void setBodyContent(String body, String contentType)
			throws MessagingException
	{
		bodyPart.setContent(body, contentType);
	}

	public void setBody(String body, String charset) throws MessagingException
	{
		bodyPart.setText(body, charset);
		this.charset = charset;
	}

	/**
	 * �����ʼ�����Ҫ�����ú��ʼ�����
	 * 
	 * @throws MessagingException
	 */
	public void send() throws MessagingException
	{
		if (sendDate == null)
			sendDate = new Date();

		// ��message part�����´�����Multipart
		Multipart mp = new MimeMultipart();

		// �ʼ����ݵĵ�һ����
		mp.addBodyPart(bodyPart);

		// �ʼ����ݵĵڶ�����
		for (int i = 0; i < files.size(); i++)
		{
			MimeBodyPart mbp = new MimeBodyPart();
			String filename = (String) files.get(i);

			FileDataSource fds = new FileDataSource(filename);
			mbp.setDataHandler(new DataHandler(fds));
			mbp.setFileName(fds.getName());
			mbp.setDescription(description, charset);

			mp.addBodyPart(mbp);
		}

		// ��MultiPart�����ʼ�
		mm.setContent(mp);
		mm.setSentDate(sendDate);
		Transport.send(mm);
	}

	/**
	 * ���ͼ��ʼ�
	 * 
	 * @param to
	 * @param from
	 * @param subject
	 * @param body
	 * @throws Exception
	 */
	public void send(String to, String from, String subject, String body)
			throws Exception
	{
		setRecipient(to);
		setFrom(from);
		setSubject(subject);
		setBody(body);

		send();
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}
}

class SmtpAuthenticator extends Authenticator
{
	private String account;

	private String password;

	/**
	 * @param account
	 * @param password
	 */
	public SmtpAuthenticator(String account, String password)
	{
		this.account = account;
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(account, password);
	}

}
