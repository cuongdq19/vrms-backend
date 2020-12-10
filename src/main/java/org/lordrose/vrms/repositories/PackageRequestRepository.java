package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.PackageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRequestRepository extends JpaRepository<PackageRequest, Long> {
}
