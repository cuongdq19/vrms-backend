package org.lordrose.vrms.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lordrose.vrms.domains.MaintenanceReminder;
import org.lordrose.vrms.repositories.MaintenanceReminderRepository;
import org.lordrose.vrms.services.impl.FirebaseNotificationServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageSendingScheduler {

    private final MaintenanceReminderRepository reminderRepository;

    private final FirebaseNotificationServiceImpl notificationService;

    @Scheduled(cron = "0 0 7 ? * *")
    public void sendRemindingAtReminder() {
        List<MaintenanceReminder> remindingAtReminders =
                reminderRepository.findAllByIsActiveTrueAndRemindAt(LocalDate.now());
        List<MaintenanceReminder> oneMonthInAdvanceReminders =
                reminderRepository.findAllByIsActiveTrueAndMaintenanceDate(LocalDate.now().plusDays(30));
        List<MaintenanceReminder> oneDayPriorReminders =
                reminderRepository.findAllByIsActiveTrueAndMaintenanceDate(LocalDate.now().minusDays(1));

        log.info("remindingAtReminders: " + remindingAtReminders.size());
        log.info("oneMonthInAdvanceReminders: " + oneMonthInAdvanceReminders.size());
        log.info("oneDayPriorReminders: " + oneDayPriorReminders.size());

        remindingAtReminders.stream()
                .filter(reminder -> !oneDayPriorReminders.contains(reminder))
                .forEach(notificationService::sendRemindAtReminderNotification);
        oneMonthInAdvanceReminders.stream()
                .filter(reminder -> !oneDayPriorReminders.contains(reminder))
                .forEach(notificationService::sendOneMonthInAdvanceReminderNotification);
        oneDayPriorReminders.stream()
                .peek(reminder -> reminder.setIsActive(false))
                .forEach(notificationService::sendOneDayPriorReminderNotification);

        reminderRepository.saveAll(oneDayPriorReminders);
    }

    /*@Scheduled(cron = "0 0/1 * ? * *")
    public void sendRequestBeginReminder() {
    }*/

    /*@Scheduled(cron = "0 0/1 * ? * *")
    public void reSendFailMessage() {
        List<Notification> failedNotifications =
                notificationRepository.findAllByIsSentFalseAndNotifyAtBefore(LocalDateTime.now());

        failedNotifications
                .forEach(notificationService::resendFailedMessage);

        log.info("failedNotifications: " + failedNotifications.size());
    }*/
}
