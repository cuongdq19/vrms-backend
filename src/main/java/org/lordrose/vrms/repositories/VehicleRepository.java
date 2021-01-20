package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByUserId(Long userId);

    Optional<Vehicle> findByVinNumberIgnoreCase(String vinNumber);

    Optional<Vehicle> findByPlateNumberIgnoreCase(String plateNumber);
}
