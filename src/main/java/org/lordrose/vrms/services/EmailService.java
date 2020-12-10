package org.lordrose.vrms.services;

public interface EmailService {

    void sendMail(String to, String subject, String text);
}
