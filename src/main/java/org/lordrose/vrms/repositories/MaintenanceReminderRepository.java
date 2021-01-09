package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.MaintenanceReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceReminderRepository extends JpaRepository<MaintenanceReminder, Long> {

    List<MaintenanceReminder> findAllByIsActiveTrueAndRemindAt(LocalDate remindAt);
}
