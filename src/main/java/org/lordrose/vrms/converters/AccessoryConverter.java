package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.models.responses.AccessoryResponse;
import org.lordrose.vrms.models.responses.ReminderResponse;
import org.lordrose.vrms.models.responses.ServiceAccessoriesResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.PartConverter.toEmptyModelPartResponse;

public class AccessoryConverter {

    private static AccessoryResponse toAccessoryResponse(ServiceRequestPart requestPart) {
        return AccessoryResponse.builder()
                .id(requestPart.getId())
                .quantity(requestPart.getQuantity())
                .reminders(requestPart.getReminders().stream()
                        .map(reminder -> ReminderResponse.builder()
                                .id(reminder.getId())
                                .remindDate(reminder.getRemindAt().toString())
                                .maintenanceDate(reminder.getMaintenanceDate())
                                .build())
                        .collect(Collectors.toList()))
                .part(toEmptyModelPartResponse(requestPart.getVehiclePart()))
                .build();
    }

    public static ServiceAccessoriesResponse toServiceAccessories(ServiceRequest serviceRequest,
                                                                  List<ServiceRequestPart> requestParts) {
        return ServiceAccessoriesResponse.builder()
                .serviceRequestId(serviceRequest.getId())
                .requestId(serviceRequest.getRequest().getId())
                .serviceName(serviceRequest.getServiceName())
                .serviceNote(serviceRequest.getNote())
                .isIncurred(serviceRequest.getIsIncurred())
                .accessories(requestParts.stream()
                        .map(AccessoryConverter::toAccessoryResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
