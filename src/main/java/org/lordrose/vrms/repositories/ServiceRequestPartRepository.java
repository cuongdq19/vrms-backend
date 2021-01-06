package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.constants.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestPartRepository extends JpaRepository<ServiceRequestPart, Long> {

    List<ServiceRequestPart> findAllByServiceRequest_Request_Vehicle_Id(Long vehicleId);

    List<ServiceRequestPart> findAllByServiceRequestIsActiveAndServiceRequest_Request_StatusAndServiceRequest_Request_Vehicle_Id(
            boolean isActive, RequestStatus requestStatus, Long vehicleId);
}
