package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.MaintenanceReminder;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.models.responses.ReminderResponse;
import org.lordrose.vrms.repositories.MaintenanceReminderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.utils.DateTimeUtils.toLocalDateTime;

@RequiredArgsConstructor
@Service
public class ReminderServiceImpl {

    private final MaintenanceReminderRepository reminderRepository;

    public void createReminders(Collection<ServiceRequestPart> requestParts) {
        List<MaintenanceReminder> reminders = new ArrayList<>();

        requestParts.stream()
                .filter(ServiceRequestPart::isAccessory)
                .forEach(requestPart -> reminders.addAll(createReminders(requestPart)));

        reminderRepository.saveAll(reminders);
    }

    private List<MaintenanceReminder> createReminders(ServiceRequestPart requestPart) {
        int duration = requestPart.getVehiclePart().getWarrantyDuration();
        int monthsPerMaintenance = requestPart.getVehiclePart().getMonthsPerMaintenance();
        if (duration == 0 || monthsPerMaintenance == 0) {
            return Collections.emptyList();
        }
        List<MaintenanceReminder> reminders = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = monthsPerMaintenance; i <= duration; i += monthsPerMaintenance) {
            reminders.add(MaintenanceReminder.builder()
                    .remindAt(today.plusMonths(i).minusDays(3))
                    .maintenanceDate(today.plusMonths(i))
                    .isActive(true)
                    .requestPart(requestPart)
                    .build());
        }
        return reminders;
    }

    public Object updateReminderDate(Long reminderId, Long remindDate) {
        MaintenanceReminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> newExceptionWithId(reminderId));

        reminder.setRemindAt(toLocalDateTime(remindDate).toLocalDate());

        MaintenanceReminder result = reminderRepository.save(reminder);

        return ReminderResponse.builder()
                .id(result.getId())
                .remindDate(result.getRemindAt().toString())
                .maintenanceDate(result.getMaintenanceDate())
                .build();
    }
}
