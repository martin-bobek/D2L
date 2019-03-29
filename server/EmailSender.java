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

/**
 * A class used to send email's from the server.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
class EmailSender {
	/**
	 * The address of the sender.
	 */
	private InternetAddress sender;
	/**
	 * The connection to the email server.
	 */
	private Session session;
	/**
	 * The message currently being constructed.
	 */
	private Message message;
	
	/**
	 * Creates a new connection to the email server.
	 * @param sender The email address of the sender.
	 * @param password The password to the senders email account.
	 * @throws AddressException The email address entered is not valid.
	 */
	void startSession(String sender, String password) throws AddressException {
		Properties properties = getProperties();
		this.sender = new InternetAddress(sender);
		session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		});
	}

	/**
	 * Sends the message which was previously constructed.
	 * @throws MessagingException Failed to send email message.
	 */
	void sendMessage() throws MessagingException {
		Transport.send(message);
	}
	
	/**
	 * Adds recipients to the current message.
	 * @param recipients A list of recipients.
	 * @throws AddressException The email address entered is not valid. 
	 * @throws MessagingException Failed to send email message.
	 */
	void setRecipients(ArrayList<String> recipients) throws AddressException, MessagingException {
		if (recipients.size() == 1)
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipients.get(0)));
		else
			for (String recipient : recipients)
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress(recipient));
	}
	
	/**
	 * Sets the content and subject of a message.
	 * @param subject The message subject.
	 * @param content The message content.
	 * @throws MessagingException Failed to send email message.
	 */
	void setContent(String subject, String content) throws MessagingException {
		message = new MimeMessage(session);
		message.setFrom(sender);
		message.setSubject(subject);
		message.setText(content);
	}
	
	/**
	 * Creates the properties for a new connection to the email server.
	 * @return The properties object.
	 */
	private Properties getProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		return properties;
	}
}
