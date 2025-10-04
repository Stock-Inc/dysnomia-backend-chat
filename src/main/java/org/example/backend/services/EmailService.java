package org.example.backend.services;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    public void sendEmail(String to, String text){
        Email email = new SimpleEmail();
        email.setHostName("");
        email.setSmtpPort(111);
        email.setAuthenticator(new DefaultAuthenticator("", ""));
        email.setSSLOnConnect(true);
        email.setSubject("");
        try {
            email.setFrom("");
            email.setMsg(text);
            email.addTo(to);
        } catch (EmailException ex) {
            System.out.println("");
        }
        try {
            email.send();
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }

    }
}
