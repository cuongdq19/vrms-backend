package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.MaintenanceLevelDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceLevelDetailRepository extends JpaRepository<MaintenanceLevelDetail, Long> {

    List<MaintenanceLevelDetail> findAllByModelIdAndProviderId(Long modelId, Long providerId);

    List<MaintenanceLevelDetail> findAllByModelIdAndLevelId(Long modelId, Long levelId);

    Optional<MaintenanceLevelDetail> findMaintenanceLevelDetailByLevelIdAndModelIdAndProviderId(
            Long levelId, Long typeId, Long providerId);
}
