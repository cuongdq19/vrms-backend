package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.MaintenanceReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceReminderRepository extends JpaRepository<MaintenanceReminder, Long> {
}
