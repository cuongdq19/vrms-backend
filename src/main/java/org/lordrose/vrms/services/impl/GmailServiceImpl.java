package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.services.EmailService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GmailServiceImpl implements EmailService {


    @Override
    public void sendMail(String to, String subject, String text) {

    }
}
