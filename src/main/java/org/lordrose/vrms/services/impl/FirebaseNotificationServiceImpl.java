package org.lordrose.vrms.services.impl;

import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Notification;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class FirebaseNotificationServiceImpl {

    private final FirebaseMessageServiceImpl messageService;
    private final NotificationRepository notificationRepository;

    public void sendCreateNotification(Request request) {
        final String body = "A new booking request is received with id: " + request.getId();
        Notification systemNotification = Notification.builder()
                .title("Incoming booking request.")
                .content(body)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(null)
                .build();

        sendTopicMessage(request, systemNotification, notificationRepository,
                "CREATE_REQUEST_" + request.getId());
    }

    public void sendCancelNotification(Request request) {
        final String body = "Your request with id: " +
                request.getId() + " is canceled.";
        final String topicBody = "The request with id: " +
                request.getId() + " is canceled.";

        Notification systemNotification = Notification.builder()
                .title("Your request: " + request.getId() + " is canceled.")
                .content(body)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(request.getVehicle().getUser())
                .build();
        Notification topicNotification = Notification.builder()
                .title("")
                .content(topicBody)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(null)
                .build();

        sendMessage(request, systemNotification, notificationRepository,
                "CANCEL_REQUEST_" + request.getId());
        sendTopicMessage(request, topicNotification, notificationRepository,
                "CANCEL_REQUEST_" + request.getId());
    }

    public void sendUpdateNotification(Request request) {
        final String body = "Provider " + request.getProvider().getName() +
                " has changes your request content.";
        Notification systemNotification = Notification.builder()
                .title("There are changes to your booking request numbered: " +
                        request.getId() + " at " + LocalTime.now().toString())
                .content(body)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(request.getVehicle().getUser())
                .build();

        sendMessage(request, systemNotification, notificationRepository,
                "UPDATE_REQUEST_" + request.getId());
    }

    public void sendFinishRepairNotification(Request request) {
        Double totalCost = request.getServices().stream()
                .mapToDouble(service ->
                        service.getPrice() +
                                service.getRequestParts().stream()
                                        .mapToDouble(part -> part.getQuantity() * part.getPrice())
                                        .sum())
                .sum();
        final String body = "Your car's repair/maintenance is completed." +
                "Please retrieve your car at " + request.getProvider().getName() +
                ". Total cost is: " +
                String.format("%,.2f", totalCost) + " VND.";
        Notification systemNotification = Notification.builder()
                .title("The repair/maintenance is finished.")
                .content(body)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(request.getVehicle().getUser())
                .build();

        sendMessage(request, systemNotification, notificationRepository,
                "FINISH_REQUEST_" + request.getId());
    }

    public void sendCheckoutNotification(Request request) {
        final String body = "Your booking is completed. " +
                "Please give us your feedback and ratings about: " +
                request.getProvider().getName() + ".";

        Notification systemNotification = Notification.builder()
                .title("Your car booking is finished.")
                .content(body)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(request.getVehicle().getUser())
                .build();

        sendMessage(request, systemNotification, notificationRepository,
                "CHECKOUT_REQUEST_" + request.getId());
    }

    private void sendMessage(Request request,
                             Notification notification,
                             NotificationRepository notificationRepository,
                             String clickAction) {
        if (validateDeviceToken(request)) {
            Message message = Message.builder()
                    .setToken(request.getVehicle().getUser().getDeviceToken())
                    .setNotification(notification.toFirebaseNotification())
                    .setAndroidConfig(notification.toAndroidConfig(clickAction))
                    .build();
            notification.setIsSent(messageService.pushNotification(message));
        }
        notificationRepository.save(notification);
    }

    private void sendTopicMessage(Request request,
                                  Notification notification,
                                  NotificationRepository notificationRepository,
                                  String action) {
        if (validateDeviceToken(request)) {
            Message message = Message.builder()
                    .setTopic("ProviderId_" + request.getProvider().getId())
                    .setNotification(notification.toFirebaseNotification())
                    .putData("action", action)
                    .build();
            notification.setIsSent(messageService.pushNotification(message));
        }
        notificationRepository.save(notification);
    }

    public void subscribeProviderTopic(String deviceToken, Long providerId) {
        messageService.subscribeProviderTopic(deviceToken, providerId);
    }

    private boolean validateDeviceToken(Request request) {
        return request.getVehicle().getUser().getDeviceToken() != null &&
                !"".equals(request.getVehicle().getUser().getDeviceToken());
    }
}
