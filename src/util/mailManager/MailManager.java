package util.mailManager;

import java.io.File;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public final class MailManager {
	// Fields
	private static final String SENDER = "itfilmoteka@gmail.com";
	private static final String SENDER_PASS = "itfilmoteka123";

	// Constructors
	private MailManager() {

	}

	// Methods
	public static void sendEmail(String receiver, String messageSubject, String content, File attachment) {

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SENDER, SENDER_PASS);
			}
		});

		try {
			// Create message
			Message message = new MimeMessage(session);

			// Set From
			message.setFrom(new InternetAddress(SENDER));

			// Set recepient
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));

			// Set subject and body
			message.setSubject(messageSubject);

			// Create a multipart object for the email
			Multipart multipart = new MimeMultipart();

			// Create a text body part and set the email content
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(content);

			// Add the text part to the email
			multipart.addBodyPart(messageBodyPart);

			// Check if the attached file is not null and add an attachment if necessary
			if (attachment != null) {
				BodyPart fileBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment);
				fileBodyPart.setDataHandler(new DataHandler(source));
				fileBodyPart.setFileName(attachment.getName());
				multipart.addBodyPart(fileBodyPart);
			}

			// Send the complete message parts
			message.setContent(multipart);

			Transport.send(message);

		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
