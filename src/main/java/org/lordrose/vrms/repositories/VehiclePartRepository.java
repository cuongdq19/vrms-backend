package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.domains.VehiclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiclePartRepository extends JpaRepository<VehiclePart, Long> {

    List<VehiclePart> findAllByProviderId(Long providerId);

    List<VehiclePart> findAllByProviderIdAndModelsContains(Long providerId, VehicleModel model);

    List<VehiclePart> findAllByProviderIdAndCategoryIdAndModelsContains(Long providerId,
                                                                        Long categoryId,
                                                                        VehicleModel model);

    List<VehiclePart> findAllByServices_IdAndModelsContains(Long serviceId, VehicleModel model);

    List<VehiclePart> findAllByServicesContainsAndModelsContains(Service service, VehicleModel model);

    boolean existsByCategoryIdAndProviderId(Long categoryId, Long providerId);
}
