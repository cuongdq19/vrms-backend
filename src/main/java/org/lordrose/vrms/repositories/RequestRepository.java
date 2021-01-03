package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.constants.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findFirstByVehicleIdAndCheckoutTimeNotNullOrderByCheckoutTimeDesc(Long vehicleId);

    List<Request> findAllByVehicle_User_Id(Long userId);

    List<Request> findAllByVehicleId(Long vehicleId);

    List<Request> findAllByProviderOrderByBookingTimeDesc(Provider provider);

    Long countAllByBookingTimeEqualsAndStatus(LocalDateTime bookingTime, RequestStatus status);

    List<Request> findAllByProviderIdAndBookingTime(Long providerId, LocalDateTime bookingTime);
}
