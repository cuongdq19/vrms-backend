package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Accessory;
import org.lordrose.vrms.domains.IncurredPart;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.repositories.AccessoryRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccessoryServiceImpl {

    private final AccessoryRepository accessoryRepository;

    public int registerAccessoryFromPartRequests(User vehicleOwner,
                                                 Collection<IncurredPart> parts) {
        List<Accessory> accessories = parts.stream()
                .map(incurredPart -> Accessory.builder()
                        .quantity(incurredPart.getQuantity())
                        .part(incurredPart.getVehiclePart())
                        .user(vehicleOwner)
                        .build())
                .collect(Collectors.toList());

        return accessoryRepository.saveAll(accessories).size();
    }

    public int registerAccessoryFromServiceRequestParts(User vehicleOwner,
                                                        Collection<ServiceRequestPart> parts) {
        List<Accessory> accessories = parts.stream()
                .map(part -> Accessory.builder()
                        .quantity(part.getQuantity())
                        .part(part.getVehiclePart())
                        .user(vehicleOwner)
                        .build())
                .collect(Collectors.toList());

        return accessoryRepository.saveAll(accessories).size();
    }
}