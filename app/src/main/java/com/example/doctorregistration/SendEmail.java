package com.example.doctorregistration;


import java.util.Properties;
import android.os.AsyncTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * This class takes care of sending emails to the user regarding their account status
 */
public class SendEmail {

    public void sendEmail(String email, String message) {
        final String username = "tellewellness@gmail.com";
        final String password = "qhae ymcf boin jebj";
        final String receiverEmail = email;
        String messageToSend = message;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        new SendMailTask(session, username, receiverEmail, messageToSend).execute();
    }

    private static class SendMailTask extends AsyncTask<Void, Void, Void> {
        private Session session;
        private String username;
        private String receiverEmail;
        private String messageToSend;

        public SendMailTask(Session session, String username, String receiverEmail, String messageToSend) {
            this.session = session;
            this.username = username;
            this.receiverEmail = receiverEmail;
            this.messageToSend = messageToSend;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
                message.setSubject("Tellewellness Registration Request Update");
                message.setText(messageToSend);
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

}
