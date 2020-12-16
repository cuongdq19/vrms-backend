package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long> {

    List<VehicleModel> findAllByManufacturerId(Long manufacturerId);

    List<VehicleModel> findDistinctByManufacturerId(Long manufacturerId);

    List<VehicleModel> findAllByManufacturerIdAndNameIgnoreCase(Long manufacturerId, String name);

    List<VehicleModel> findALlByIdNotIn(Set<Long> modelIds);
}
