package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.ServiceVehiclePart;
import org.lordrose.vrms.domains.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceVehiclePartRepository extends JpaRepository<ServiceVehiclePart, Long> {

    List<ServiceVehiclePart> findTop10AllByPartCategorySectionIdAndPartModelsContains(Long sectionId,
                                                                                      VehicleModel model);

    List<ServiceVehiclePart> findAllByPartCategoryIdAndPartIsDeletedAndPartModelsContains(
            Long categoryId, boolean isDeleted, VehicleModel model);
}
