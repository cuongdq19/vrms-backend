package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Set<Notification> findAllByNotifyAtAfterAndIsActiveTrue(LocalDateTime currentTime);
}
