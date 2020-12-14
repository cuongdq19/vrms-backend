package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findAllByTypeDetailIdAndModelGroup_Models_Id(
            Long typeDetailId, Long modelId);

    List<Service> findAllByTypeDetailPartCategory_IdAndModelGroup_Models_Id(
            Long categoryId, Long modelId);

    List<Service> findAllByTypeDetailIdAndProviderId(Long typeDetailId, Long providerId);

    List<Service> findAllByProviderIdAndModelGroup_Models_Id(Long providerId, Long modelId);

    List<Service> findAllByProviderId(Long providerId);

    List<Service> findAllByProviderIdAndTypeDetailType(Long providerId, ServiceType type);
}
