package org.lordrose.vrms.services.impl;

import com.google.firebase.messaging.Message;
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

    public void sendCheckoutNotification(Request request) {
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
                String.format("%.2f", totalCost) + " VND";
        Notification systemNotification = Notification.builder()
                .title("The repair/maintenance is finished.")
                .content(body)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(request.getVehicle().getUser())
                .build();

        if (request.getVehicle().getUser().getDeviceToken() != null &&
                !"".equals(request.getVehicle().getUser().getDeviceToken())) {
            Message message = Message.builder()
                    .setToken(request.getVehicle().getUser().getDeviceToken())
                    .setNotification(systemNotification.toFirebaseNotification())
                    .build();
            systemNotification.setIsSent(messageService.pushNotification(message));
        }
        notificationRepository.save(systemNotification);
    }
}
