package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Accessory;
import org.lordrose.vrms.domains.ServiceRequest;
import org.lordrose.vrms.models.responses.AccessoryResponse;
import org.lordrose.vrms.models.responses.ServiceAccessoriesResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.PartConverter.toEmptyModelPartResponse;

public class AccessoryConverter {

    private static AccessoryResponse toAccessoryResponse(Accessory accessory) {
        return AccessoryResponse.builder()
                .id(accessory.getId())
                .quantity(accessory.getServiceRequestPart().getQuantity())
                .warrantyDuration(accessory.getWarrantyDuration())
                .monthsPerMaintenance(accessory.getMonthsPerMaintenance())
                .installedDate(accessory.getInstalledDate().toString())
                .part(toEmptyModelPartResponse(accessory.getServiceRequestPart().getVehiclePart()))
                .build();
    }

    private static ServiceAccessoriesResponse toServiceAccessories(ServiceRequest serviceRequest,
                                                                   Collection<Accessory> accessories) {
        return ServiceAccessoriesResponse.builder()
                .serviceRequestId(serviceRequest.getId())
                .requestId(serviceRequest.getRequest().getId())
                .serviceName(serviceRequest.getServiceName())
                .serviceNote(serviceRequest.getNote())
                .isIncurred(serviceRequest.getIsIncurred())
                .accessories(accessories.stream()
                        .map(AccessoryConverter::toAccessoryResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public static List<ServiceAccessoriesResponse> toAccessoryResponses(Collection<Accessory> accessories) {
        List<ServiceAccessoriesResponse> responses = new ArrayList<>();

        accessories.stream()
                .collect(Collectors.groupingBy(accessory ->
                            accessory.getServiceRequestPart().getServiceRequest()))
                .forEach((serviceRequest, accessoryList) ->
                        responses.add(toServiceAccessories(serviceRequest, accessoryList)));

        return responses;
    }
}
