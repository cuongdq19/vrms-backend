package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.BasePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasePackageRepository extends JpaRepository<BasePackage, Long> {
}
