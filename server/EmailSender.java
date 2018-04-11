package server;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class EmailSender {
	private InternetAddress sender;
	private Session session;
	private Message message;
	
	void startSession(String sender, String password) throws AddressException {
		Properties properties = getProperties();
		this.sender = new InternetAddress(sender);
		session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		});
	}

	void sendMessage() throws MessagingException {
		Transport.send(message);
	}
	
	void setRecipients(ArrayList<String> recipients) throws AddressException, MessagingException {
		if (recipients.size() == 1)
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipients.get(0)));
		else
			for (String recipient : recipients)
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress(recipient));
	}
	
	void setContent(String subject, String content) throws MessagingException {
		message = new MimeMessage(session);
		message.setFrom(sender);
		message.setSubject(subject);
		message.setText(content);
	}
	
	private Properties getProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		return properties;
	}
}
