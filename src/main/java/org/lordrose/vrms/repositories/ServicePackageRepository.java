package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {

    List<ServicePackage> findAllByProviderId(Long providerId);
}
