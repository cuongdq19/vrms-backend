package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long> {

    List<VehicleModel> findAllByManufacturerId(Long manufacturerId);

    VehicleModel findAllById(Long vehicleId);
}
