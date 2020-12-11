package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.PartSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartSectionRepository extends JpaRepository<PartSection, Long> {
}
