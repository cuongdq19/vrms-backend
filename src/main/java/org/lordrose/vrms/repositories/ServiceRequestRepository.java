package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findAllByRequestIdAndIdIn(Long requestId, Collection<Long> ids);
}
