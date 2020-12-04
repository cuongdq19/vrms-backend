package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.RequestPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPartRepository extends JpaRepository<RequestPart, Long> {
}
