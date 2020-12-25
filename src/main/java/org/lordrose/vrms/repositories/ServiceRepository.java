package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findAllByTypeDetailIdAndProviderId(Long typeDetailId, Long providerId);

    List<Service> findAllByProviderId(Long providerId);

    List<Service> findAllByProviderIdAndTypeDetailType(Long providerId, ServiceType type);

    List<Service> findAllByProviderIdAndTypeDetailType_Id(Long providerId, Long typeId);

    List<Service> findAllByTypeDetailIdAndPartSet_Part_Models_Id(Long typeDetailId, Long modelId);

    boolean existsByPartSet_Part_Id(Long partId);
}
