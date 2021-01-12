package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.domains.VehiclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiclePartRepository extends JpaRepository<VehiclePart, Long> {

    List<VehiclePart> findAllByProviderIdAndIsDeletedFalse(Long providerId);

    List<VehiclePart> findAllByProviderIdAndIsDeletedFalseAndModelsContains(Long providerId, VehicleModel model);

    List<VehiclePart> findAllByProviderIdAndIsDeletedFalseAndCategoryIdAndModelsContains(
            Long providerId, Long categoryId, VehicleModel model);

    List<VehiclePart> findAllByCategoryIdAndProviderIdAndIsDeletedFalse(Long categoryId, Long providerId);
}
