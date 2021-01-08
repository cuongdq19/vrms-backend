package org.lordrose.vrms.services.impl;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Notification;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
                .user(request.getVehicle().getUser())
                .build();

        sendTopicMessage(request, systemNotification, notificationRepository);
    }

    public void sendUpdateNotification(Request request) {
        final String body = "Provider " + request.getProvider().getName() +
                " has changes your request content.";
        Notification systemNotification = Notification.builder()
                .title("There are changes to your booking request.")
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
                                  NotificationRepository notificationRepository) {
        if (validateDeviceToken(request)) {
            Message message = Message.builder()
                    .setTopic("ProviderId_" + request.getProvider().getId())
                    .setNotification(notification.toFirebaseNotification())
                    .setWebpushConfig(WebpushConfig.builder()
                            .putHeader("action", "CREATE_REQUEST_" + request.getId())
                            .build())
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
