package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Accessory;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.models.responses.ServiceAccessoriesResponse;
import org.lordrose.vrms.repositories.AccessoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.AccessoryConverter.toAccessoryResponses;

@RequiredArgsConstructor
@Service
public class AccessoryServiceImpl {

    private final AccessoryRepository accessoryRepository;

    public void registerAccessoryFromServiceRequests(Collection<ServiceRequest> services) {
        List<Accessory> accessories = services.stream()
                .filter(service -> {
                    if (service.getRequestParts() != null)
                        return !service.getRequestParts().isEmpty();
                    return false;
                })
                .flatMap(service -> service.getRequestParts().stream())
                .filter(ServiceRequestPart::isAccessory)
                .map(requestPart -> Accessory.builder()
                        .serviceRequestPart(requestPart)
                        .warrantyDuration(requestPart.getVehiclePart().getWarrantyDuration())
                        .monthsPerMaintenance(requestPart.getVehiclePart().getMonthsPerMaintenance())
                        .installedDate(LocalDate.now())
                        .build())
                .collect(Collectors.toList());

        accessoryRepository.saveAll(accessories);
    }

    public List<ServiceAccessoriesResponse> findAllByVehicle(Long vehicleId) {
        return toAccessoryResponses(
                accessoryRepository.findAllByServiceRequestPart_ServiceRequest_Request_Vehicle_Id(vehicleId));
    }
}
