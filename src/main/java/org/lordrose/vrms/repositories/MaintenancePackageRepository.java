package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.MaintenancePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenancePackageRepository extends JpaRepository<MaintenancePackage, Long> {

    List<MaintenancePackage> findDistinctByPackagedServices_Provider_Id(Long providerId);
}
