package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.IncurredPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRequestRepository extends JpaRepository<IncurredPart, Long> {
}
