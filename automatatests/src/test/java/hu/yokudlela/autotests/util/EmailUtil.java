package hu.yokudlela.autotests.util;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author krisztian
 */
public class EmailUtil {

    private static String SMTPHOST = "smtp.mailgun.org";
    private static String MAILAUTHUSER = "postmaster@sandbox81d50caaf075480aafce0a16f1333128.mailgun.org";
    private static String MAILAUTHPASSWORD = "ff8dae0470b458fdcab95b575a545dc1-18e06deb-1c4695a2";
    private static String MAILSENDER = "postmaster@sandbox81d50caaf075480aafce0a16f1333128.mailgun.org";

    public void send(String pDestination, String pSubject, String pRequest, String pResponse) {
        Properties props = new Properties();
        props.put("mail.smtp.user", MAILAUTHUSER);
        props.put("mail.smtp.host", SMTPHOST);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", 587);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        SecurityManager security = System.getSecurityManager();
        String mailText = "Request: " + pRequest + "<br > Response: " + pResponse;

        for (String destMail : pDestination.split(",")) {

            try {

                Authenticator auth = new SMTPAuthenticator();
                Session session = Session.getInstance(props, auth);
                MimeMessage msg = new MimeMessage(session);
                msg.setContent(mailText, "text/html; charset=utf-8");
                msg.setSubject(pSubject);
                msg.setFrom(new InternetAddress(MAILAUTHUSER));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(destMail));

                Transport.send(msg);
            } catch (Exception mex) {
                mex.printStackTrace();
            }
        }
    }

   

    private class SMTPAuthenticator extends Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(MAILAUTHUSER, MAILAUTHPASSWORD);
        }
    }

}
