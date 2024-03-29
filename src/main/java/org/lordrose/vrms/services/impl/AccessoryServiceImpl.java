package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.MaintenanceReminder;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.constants.RequestStatus;
import org.lordrose.vrms.models.responses.AccessoryResponse;
import org.lordrose.vrms.models.responses.ReminderResponse;
import org.lordrose.vrms.repositories.ServiceRequestPartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.PartConverter.toEmptyModelPartResponse;

@RequiredArgsConstructor
@Service
public class AccessoryServiceImpl {

    private final ServiceRequestPartRepository requestPartRepository;

    @Transactional
    public List<AccessoryResponse> findAllByVehicle(Long vehicleId) {
        List<AccessoryResponse> responses = new ArrayList<>();

        requestPartRepository.findAllByServiceRequestIsActiveAndServiceRequest_Request_StatusAndServiceRequest_Request_Vehicle_Id(
                true, RequestStatus.FINISHED, vehicleId).stream()
                .filter(ServiceRequestPart::isAccessory)
                .collect(Collectors.groupingBy(requestPart -> requestPart.getServiceRequest().getRequest()))
                .forEach(((request, requestParts) -> {
                    AccessoryCollection collection = new AccessoryCollection();
                    collection.addAll(requestParts);
                    responses.addAll(collection.getResults());
                }));

        return responses;
    }

    private static class AccessoryCollection {
        private final List<AccessoryResponse> storage = new ArrayList<>();

        public void add(ServiceRequestPart requestPart) {
            for (AccessoryResponse accessory : storage) {
                if (accessory.getPart().getId().equals(requestPart.getVehiclePart().getId())) {
                    accessory.addQuantity(requestPart.getQuantity());
                }
            }
            AccessoryResponse response = AccessoryResponse.builder()
                    .quantity(requestPart.getQuantity())
                    .reminder(requestPart.getReminders().stream()
                            .filter(MaintenanceReminder::getIsActive)
                            .sorted(Comparator.comparing(MaintenanceReminder::getMaintenanceDate))
                            .map(reminder -> ReminderResponse.builder()
                                    .id(reminder.getId())
                                    .remindDate(reminder.getRemindAt().toString())
                                    .maintenanceDate(reminder.getMaintenanceDate())
                                    .build())
                            .findFirst().orElse(null))
                    .part(toEmptyModelPartResponse(requestPart.getVehiclePart()))
                    .build();
            if (response.getReminder() != null) {
                storage.add(response);
            }
        }

        public void addAll(Collection<ServiceRequestPart> requestParts) {
            requestParts.forEach(this::add);
        }

        public List<AccessoryResponse> getResults() {
            return storage;
        }
    }
}
