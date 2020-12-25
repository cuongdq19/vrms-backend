package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findAllByProviderId(Long providerId);

    List<Service> findAllByProviderIdAndTypeDetailType(Long providerId, ServiceType type);

    List<Service> findAllByProviderIdAndPartSet_Part_Models_Id(Long providerId, Long modelId);

    List<Service> findAllByProviderIdAndPartSet_Part_Models_IdAndPartSet_Part_Id(
            Long providerId, Long modelId, Long partId);

    List<Service> findAllByTypeDetailIdAndPartSet_Part_Models_Id(Long typeDetailId, Long modelId);
}
