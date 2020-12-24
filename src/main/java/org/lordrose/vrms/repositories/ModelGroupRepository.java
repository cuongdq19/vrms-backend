package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.ModelGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelGroupRepository extends JpaRepository<ModelGroup, Long> {

    List<ModelGroup> findByModels_Id(Long modelId);
}
