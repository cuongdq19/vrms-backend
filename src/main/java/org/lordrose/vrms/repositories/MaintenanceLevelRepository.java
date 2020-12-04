package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.MaintenanceLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceLevelRepository extends JpaRepository<MaintenanceLevel, Long> {

    List<MaintenanceLevel> findAllByManufacturerId(Long manufacturerId);
}
