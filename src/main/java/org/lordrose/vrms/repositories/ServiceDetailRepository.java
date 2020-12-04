package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.ServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceDetailRepository extends JpaRepository<ServiceDetail, Long> {

    List<ServiceDetail> findAllByServiceIdAndModelGroup_Models_Id(
            Long serviceId, Long typeId);

    List<ServiceDetail> findAllByServiceIdAndProviderId(Long serviceId, Long providerId);

    List<ServiceDetail> findAllByProviderIdAndModelGroup_Models_Id(Long providerId, Long typeId);

    List<ServiceDetail> findAllByProviderId(Long providerId);
}
