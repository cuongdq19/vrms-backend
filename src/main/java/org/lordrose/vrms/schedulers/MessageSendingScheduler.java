package org.lordrose.vrms.schedulers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.MaintenanceReminder;
import org.lordrose.vrms.repositories.MaintenanceReminderRepository;
import org.lordrose.vrms.repositories.NotificationRepository;
import org.lordrose.vrms.repositories.RequestRepository;
import org.lordrose.vrms.services.impl.FirebaseNotificationServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MessageSendingScheduler {

    private final MaintenanceReminderRepository reminderRepository;
    private final NotificationRepository notificationRepository;
    private final RequestRepository requestRepository;

    private final FirebaseNotificationServiceImpl notificationService;

    @Scheduled(cron = "0 0 7 ? * *")
    public void sendMaintenanceReminder() {
        List<MaintenanceReminder> reminders =
                reminderRepository.findAllByIsActiveTrueAndRemindAt(LocalDate.now());

        reminders.forEach(notificationService::sendMaintenanceReminderNotification);
    }

    /*@Scheduled(cron = "")
    public void sendRequestBeginReminder() {
    }

    @Scheduled(cron = "")
    public void reSendFailMessage() {
    }*/
}
