package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.PartCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartCategoryRepository extends JpaRepository<PartCategory, Long> {

    List<PartCategory> findAllBySectionId(Long sectionId);

    Optional<PartCategory> findByNameEqualsIgnoreCase(String name);
}
