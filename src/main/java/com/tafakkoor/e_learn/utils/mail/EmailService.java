package com.tafakkoor.e_learn.utils.mail;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private static final ThreadLocal<EmailService> EMAIL_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(EmailService::new);
    public void sendEmail( String email, String body, String subject) {
        String from = "strengthnumberone@gmail.com";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply.e.learn.ltd@gmail.com", "fbursjiqgneliajb");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
    public static EmailService getInstance() {
        return EMAIL_SERVICE_THREAD_LOCAL.get();
    }


    public static void main( String[] args ) {

    }
}