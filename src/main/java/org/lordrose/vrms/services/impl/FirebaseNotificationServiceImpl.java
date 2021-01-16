package org.lordrose.vrms.services.impl;

import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.MaintenanceReminder;
import org.lordrose.vrms.domains.Notification;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.repositories.MaintenanceReminderRepository;
import org.lordrose.vrms.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class FirebaseNotificationServiceImpl {

    private final FirebaseMessageServiceImpl messageService;
    private final NotificationRepository notificationRepository;
    private final MaintenanceReminderRepository reminderRepository;

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

        sendMessage(systemNotification, notificationRepository,
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

        sendMessage(systemNotification, notificationRepository,
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

        sendMessage(systemNotification, notificationRepository,
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

        sendMessage(systemNotification, notificationRepository,
                "CHECKOUT_REQUEST_" + request.getId());
    }

    public void sendRemindAtReminderNotification(MaintenanceReminder reminder) {
        final String body = "Your maintenance reminder date for part: " +
                reminder.getRequestPart().getVehiclePart().getName() + " is today.";

        Notification systemNotification = Notification.builder()
                .title("The reminder date you setup is today.")
                .content(body)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(reminder.getRequestPart().getServiceRequest().getRequest().getVehicle().getUser())
                .build();

        sendMessage(systemNotification, notificationRepository,
                "MAINTENANCE_REMINDER" + reminder.getId());
    }

    public void sendOneMonthInAdvanceReminderNotification(MaintenanceReminder reminder) {
        final String body = "Your part: " + reminder.getRequestPart().getVehiclePart().getName() +
                " maintenance date will be due after 1 month from today.";

        Notification systemNotification = Notification.builder()
                .title("Your 1 month prior reminder to maintain your part.")
                .content(body)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(reminder.getRequestPart().getServiceRequest().getRequest().getVehicle().getUser())
                .build();

        sendMessage(systemNotification, notificationRepository,
                "MAINTENANCE_REMINDER" + reminder.getId());
    }

    public void sendOneDayPriorReminderNotification(MaintenanceReminder reminder) {
        final String body = "Your part: " + reminder.getRequestPart().getVehiclePart().getName() +
                " maintenance date is due tomorrow.";

        Notification systemNotification = Notification.builder()
                .title("Your 1 day prior reminder to maintain your part.")
                .content(body)
                .notifyAt(LocalDateTime.now())
                .isSent(false)
                .user(reminder.getRequestPart().getServiceRequest().getRequest().getVehicle().getUser())
                .build();

        sendMessage(systemNotification, notificationRepository,
                "MAINTENANCE_REMINDER" + reminder.getId());
    }

    public void resendFailedMessage(Notification notification) {
        if (notification.getUser() != null) {
            sendMessage(notification, notificationRepository, "");
        }
    }

    private void sendMessage(Notification notification,
                             NotificationRepository notificationRepository,
                             String clickAction) {
        if (validateDeviceToken(notification)) {
            Message message = Message.builder()
                    .setToken(notification.getUser().getDeviceToken())
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
        Message message = Message.builder()
                .setTopic("ProviderId_" + request.getProvider().getId())
                .setNotification(notification.toFirebaseNotification())
                .putData("action", action)
                .build();
        notification.setIsSent(messageService.pushNotification(message));

        notificationRepository.save(notification);
    }

    public void subscribeProviderTopic(String deviceToken, Long providerId) {
        messageService.subscribeProviderTopic(deviceToken, providerId);
    }

    private boolean validateDeviceToken(Notification notification) {
        return notification.getUser().getDeviceToken() != null &&
                !"".equals(notification.getUser().getDeviceToken());
    }
}
