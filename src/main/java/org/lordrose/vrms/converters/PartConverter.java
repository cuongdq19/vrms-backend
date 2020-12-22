package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.PartRequest;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.responses.PartCheckoutResponse;
import org.lordrose.vrms.models.responses.PartResponse;
import org.lordrose.vrms.models.responses.PartSuggestingResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponses;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

public class PartConverter {

    public static PartResponse toEmptyModelPartResponse(VehiclePart part) {
        return PartResponse.builder()
                .id(part.getId())
                .name(part.getName())
                .description(part.getDescription())
                .price(part.getPrice())
                .warrantyDuration(part.getWarrantyDuration())
                .monthsPerMaintenance(part.getMonthsPerMaintenance())
                .imageUrls(getUrlsAsArray(part.getImageUrls()))
                .categoryId(part.getCategory().getId())
                .categoryName(part.getCategory().getName())
                .models(Collections.emptyList())
                .build();
    }

    public static List<PartResponse> toEmptyModelPartResponses(Collection<VehiclePart> parts) {
        return parts.stream()
                .map(PartConverter::toEmptyModelPartResponse)
                .collect(Collectors.toList());
    }

    public static PartResponse toPartResponse(VehiclePart part) {
        return PartResponse.builder()
                .id(part.getId())
                .name(part.getName())
                .description(part.getDescription())
                .price(part.getPrice())
                .warrantyDuration(part.getWarrantyDuration())
                .monthsPerMaintenance(part.getMonthsPerMaintenance())
                .imageUrls(getUrlsAsArray(part.getImageUrls()))
                .sectionId(part.getCategory().getSection().getId())
                .categoryId(part.getCategory().getId())
                .categoryName(part.getCategory().getName())
                .models(toModelResponses(part.getModels()))
                .build();
    }

    public static List<PartResponse> toPartResponses(Collection<VehiclePart> parts) {
        return parts.stream()
                .map(PartConverter::toPartResponse)
                .collect(Collectors.toList());
    }

    public static PartCheckoutResponse toPartDetailResponse(PartRequest part) {
        return PartCheckoutResponse.builder()
                .id(part.getId())
                .partId(part.getVehiclePart().getId())
                .partName(part.getVehiclePart().getName())
                .quantity(part.getQuantity())
                .price(part.getPrice())
                .warrantyDuration(part.getVehiclePart().getWarrantyDuration())
                .monthsPerMaintenance(part.getVehiclePart().getMonthsPerMaintenance())
                .imageUrls(getUrlsAsArray(part.getVehiclePart().getImageUrls()))
                .categoryId(part.getVehiclePart().getCategory().getId())
                .categoryName(part.getVehiclePart().getCategory().getName())
                .build();
    }

    public static List<PartCheckoutResponse> toPartDetailResponses(Collection<PartRequest> parts) {
        return parts.stream()
                .map(PartConverter::toPartDetailResponse)
                .collect(Collectors.toList());
    }

    public static PartCheckoutResponse toPartCheckoutResponse(ServiceRequestPart servicePart) {
        if (servicePart == null)
            return null;
        return PartCheckoutResponse.builder()
                .id(servicePart.getId())
                .partId(servicePart.getVehiclePart().getId())
                .partName(servicePart.getVehiclePart().getName())
                .quantity(servicePart.getQuantity())
                .price(servicePart.getPrice())
                .warrantyDuration(servicePart.getVehiclePart().getWarrantyDuration())
                .monthsPerMaintenance(servicePart.getVehiclePart().getMonthsPerMaintenance())
                .imageUrls(getUrlsAsArray(servicePart.getVehiclePart().getImageUrls()))
                .categoryId(servicePart.getVehiclePart().getCategory().getId())
                .categoryName(servicePart.getVehiclePart().getCategory().getName())
                .build();
    }

    public static PartSuggestingResponse toPartSuggestingResponse(VehiclePart part, boolean isSupported) {
        return PartSuggestingResponse.builder()
                .id(part.getId())
                .name(part.getName())
                .description(part.getDescription())
                .price(part.getPrice())
                .warrantyDuration(part.getWarrantyDuration())
                .monthsPerMaintenance(part.getMonthsPerMaintenance())
                .imageUrls(getUrlsAsArray(part.getImageUrls()))
                .sectionId(part.getCategory().getSection().getId())
                .categoryId(part.getCategory().getId())
                .categoryName(part.getCategory().getName())
                .isSupportedByService(isSupported)
                .models(Collections.emptyList())
                .build();
    }
}
