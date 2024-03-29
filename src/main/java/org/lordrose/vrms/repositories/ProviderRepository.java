package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    long countAllByCreateAtBetween(LocalDateTime from, LocalDateTime to);
}
