package org.lordrose.vrms.services.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Collections.singletonList;

@Slf4j
@Service
public class FirebaseMessageServiceImpl {

    public boolean pushNotification(Message message) {
        try {
            FirebaseMessaging.getInstance().send(message);
            return true;
        } catch (FirebaseMessagingException e) {
            log.info("Error occurred while sending message: " + e.getLocalizedMessage());
            return false;
        }
    }

    public void subscribeProviderTopic(String deviceToken, Long providerId) {
        try {
            final String providerIdTopic = "ProviderId_" + providerId;
            System.out.println(providerIdTopic);
            FirebaseMessaging.getInstance()
                    .subscribeToTopic(singletonList(deviceToken), providerIdTopic);
        } catch (FirebaseMessagingException e) {
            log.info("Error occurred while subscribing message: " + e.getLocalizedMessage());
        }
    }
}
