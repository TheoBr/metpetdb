package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.GenericServlet;

import edu.rpi.metpetdb.client.error.UnableToSendEmailException;

public class EmailSupport {
	private static final RecipientType TO = Message.RecipientType.TO;
	private static String smtpServer;
	private static InternetAddress fromAddress;
	private static Properties allProperties;

	
	public static String getHostName()
	{
		try {
			return java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "localhost";
	}
	
	public static String getProperty(String key) 
	{
		try
		{
		if (allProperties == null)
			EmailSupport.load();
		
		} catch (MessagingException me)
		{
			me.printStackTrace();
		}
		
		
		return allProperties.getProperty(key);
	}
	
	public static String getApproverAddress() throws MessagingException
	{
		if (allProperties == null)
			EmailSupport.load();
		
		return allProperties.getProperty("approver.address");
	}
	
	public static synchronized Properties load() throws MessagingException {
		if (smtpServer != null)
			return null;
		final String propFile = "email.properties";
		final InputStream i = EmailSupport.class.getClassLoader()
				.getResourceAsStream(propFile);
		if (i == null)
			throw new MessagingException("No " + propFile + " found");
		final Properties props = new Properties();
		try {
			props.load(i);
			i.close();
		} catch (IOException ioe) {
			throw new MessagingException("Cannot load " + propFile, ioe);
		}

		smtpServer = props.getProperty("smtp.server");
		fromAddress = new InternetAddress(props.getProperty("from.address",
				props.getProperty("from.name")));

		allProperties = props;
		
		return allProperties;
	}

	
	private static Session newSession() throws MessagingException {
		load();
		final Properties p = new Properties();
		p.put("mail.smtp.host", smtpServer);
		return Session.getInstance(p, null);
	}
	

	/**
	 * Send a single message to one address.
	 * 
	 * @param to
	 * 		email address to send the message to.
	 * @param mid
	 * 		identity of the message configuration properties within
	 * 		<code>email.properties</code>.
	 * @param args
	 * 		arguments for MessageFormat to insert into the message subject and
	 * 		body templates.
	 * @throws UnableToSendEmailException
	 * 		the message could not be sent, likely due to a server
	 * 		misconfiguration.
	 */
	public static void sendMessage(final GenericServlet srv, final String to,
			final String mid, final Object[] args)
			throws UnableToSendEmailException {
		try {
			final Message msg = new MimeMessage(newSession());
			final String subjFmt = allProperties.getProperty(mid + ".subject");
			final String bodyFmt = allProperties.getProperty(mid + ".body");
			msg.setFrom(fromAddress);
			msg.setRecipients(TO, new InternetAddress[] {
				new InternetAddress(to)
			});
			msg.setSubject(MessageFormat.format(subjFmt, args));
			msg.setSentDate(new Date());
			msg.setText(MessageFormat.format(bodyFmt, args));
			Transport.send(msg);
		} catch (MessagingException mex) {
			srv.log("Unable to send " + mid + " email to " + to, mex);
			throw new UnableToSendEmailException();
		}
	}

	private EmailSupport() {
	}
}
