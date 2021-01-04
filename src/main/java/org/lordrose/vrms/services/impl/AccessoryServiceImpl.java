package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.constants.RequestStatus;
import org.lordrose.vrms.models.responses.ServiceAccessoriesResponse;
import org.lordrose.vrms.repositories.ServiceRequestPartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.AccessoryConverter.toServiceAccessories;

@RequiredArgsConstructor
@Service
public class AccessoryServiceImpl {

    private final ServiceRequestPartRepository requestPartRepository;

    public List<ServiceAccessoriesResponse> findAllByVehicle(Long vehicleId) {
        List<ServiceAccessoriesResponse> responses = new ArrayList<>();

        requestPartRepository.findAllByServiceRequest_Request_StatusAndServiceRequest_Request_Vehicle_Id(
                RequestStatus.FINISHED, vehicleId).stream()
                .filter(ServiceRequestPart::isAccessory)
                .collect(Collectors.groupingBy(ServiceRequestPart::getServiceRequest))
                .forEach(((serviceRequest, requestParts) ->
                        responses.add(toServiceAccessories(serviceRequest, requestParts))));

        return responses;
    }
}
