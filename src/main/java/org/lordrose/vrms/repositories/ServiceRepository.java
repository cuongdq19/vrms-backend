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

    List<Service> findDistinctByParts_Category_Id(Long categoryId);

    List<Service> findDistinctByTypeDetailIdAndModelGroup_Models_Id(Long detailId, Long modelId);

    List<Service> findAllByProviderIdAndModelGroup_Models_IdAndParts_Id(Long providerId,
                                                                        Long modelId, Long partId);
}
