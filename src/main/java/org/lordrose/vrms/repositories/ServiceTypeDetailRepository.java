package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeDetailRepository extends JpaRepository<ServiceTypeDetail, Long> {

    List<ServiceTypeDetail> findAllByTypeId(Long typeId);
}
