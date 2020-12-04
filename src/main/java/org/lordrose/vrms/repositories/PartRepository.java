package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.domains.VehiclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<VehiclePart, Long> {

    List<VehiclePart> findAllByProviderId(Long providerId);
    List<VehiclePart> findAllByProviderIdAndModelsContains(Long providerId, VehicleModel model);
}
