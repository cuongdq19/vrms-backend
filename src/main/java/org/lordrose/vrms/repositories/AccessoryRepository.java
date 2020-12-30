package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    List<Accessory> findAllByVehicleId(Long vehicleId);
}
