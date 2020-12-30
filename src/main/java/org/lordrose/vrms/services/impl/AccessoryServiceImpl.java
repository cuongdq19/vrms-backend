package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Accessory;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.models.responses.AccessoryResponse;
import org.lordrose.vrms.repositories.AccessoryRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.AccessoryConverter.toAccessoryResponses;

@RequiredArgsConstructor
@Service
public class AccessoryServiceImpl {

    private final AccessoryRepository accessoryRepository;

    public void registerAccessoryFromServiceRequestParts(Vehicle vehicle,
                                                         Collection<ServiceRequestPart> parts) {
        List<Accessory> accessories = parts.stream()
                .map(part -> Accessory.builder()
                        .quantity(part.getQuantity())
                        .part(part.getVehiclePart())
                        .vehicle(vehicle)
                        .build())
                .collect(Collectors.toList());

        accessoryRepository.saveAll(accessories);
    }

    public List<AccessoryResponse> findAllByVehicle(Long vehicleId) {
        return toAccessoryResponses(accessoryRepository.findAllByVehicleId(vehicleId));
    }
}
