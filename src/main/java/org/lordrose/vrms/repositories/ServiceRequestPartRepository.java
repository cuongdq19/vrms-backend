package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.ServiceRequestPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestPartRepository extends JpaRepository<ServiceRequestPart, Long> {
}
