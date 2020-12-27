package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.PartRequest;
import org.lordrose.vrms.domains.ServiceRequestPart;
import org.lordrose.vrms.domains.ServiceVehiclePart;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.responses.PartCheckoutResponse;
import org.lordrose.vrms.models.responses.PartHistoryResponse;
import org.lordrose.vrms.models.responses.PartQuantityResponse;
import org.lordrose.vrms.models.responses.PartResponse;
import org.lordrose.vrms.models.responses.PartSuggestingResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponses;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

public class PartConverter {

    public static PartQuantityResponse toEmptyModelServicePartResponse(ServiceVehiclePart part) {
        return PartQuantityResponse.builder()
                .id(part.getPart().getId())
                .name(part.getPart().getName())
                .description(part.getPart().getDescription())
                .quantity(part.getQuantity())
                .price(part.getPart().getPrice())
                .warrantyDuration(part.getPart().getWarrantyDuration())
                .monthsPerMaintenance(part.getPart().getMonthsPerMaintenance())
                .imageUrls(getUrlsAsArray(part.getPart().getImageUrls()))
                .sectionId(part.getPart().getCategory().getSection().getId())
                .categoryId(part.getPart().getCategory().getId())
                .categoryName(part.getPart().getCategory().getName())
                .build();
    }

    public static List<PartQuantityResponse> toEmptyModelServicePartResponses(Collection<ServiceVehiclePart> parts) {
        if (parts == null)
            return Collections.emptyList();
        return parts.stream()
                .map(PartConverter::toEmptyModelServicePartResponse)
                .collect(Collectors.toList());
    }

    public static PartResponse toEmptyModelPartResponse(VehiclePart part) {
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

    public static List<PartCheckoutResponse> toPartCheckoutResponses(Collection<ServiceRequestPart> serviceParts) {
        return serviceParts.stream()
                .map(PartConverter::toPartCheckoutResponse)
                .collect(Collectors.toList());
    }

    public static PartSuggestingResponse toPartSuggestingResponse(VehiclePart part) {
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
                .isSupportedByService(true)
                .models(Collections.emptyList())
                .build();
    }

    public static PartHistoryResponse toPartHistoryResponse(PartRequest part) {
        return PartHistoryResponse.builder()
                .partId(part.getVehiclePart().getId())
                .quantity(part.getQuantity())
                .priceEach(part.getPrice())
                .partName(part.getVehiclePart().getName())
                .description(part.getVehiclePart().getDescription())
                .partImageUrls(getUrlsAsArray(part.getVehiclePart().getImageUrls()))
                .warrantyDuration(part.getVehiclePart().getWarrantyDuration())
                .categoryId(part.getVehiclePart().getCategory().getId())
                .categoryName(part.getVehiclePart().getCategory().getName())
                .sectionId(part.getVehiclePart().getCategory().getSection().getId())
                .sectionName(part.getVehiclePart().getCategory().getSection().getName())
                .isAccessory(part.getVehiclePart().getCategory().getIsAccessory())
                .build();
    }

    public static List<PartHistoryResponse> toPartHistoryResponses(Collection<PartRequest> parts) {
        return parts.stream()
                .map(PartConverter::toPartHistoryResponse)
                .collect(Collectors.toList());
    }

    public static PartHistoryResponse toServicePartHistoryResponse(ServiceRequestPart part) {
        return PartHistoryResponse.builder()
                .partId(part.getVehiclePart().getId())
                .quantity(part.getQuantity())
                .priceEach(part.getPrice())
                .partName(part.getVehiclePart().getName())
                .description(part.getVehiclePart().getDescription())
                .partImageUrls(getUrlsAsArray(part.getVehiclePart().getImageUrls()))
                .warrantyDuration(part.getVehiclePart().getWarrantyDuration())
                .categoryId(part.getVehiclePart().getCategory().getId())
                .categoryName(part.getVehiclePart().getCategory().getName())
                .sectionId(part.getVehiclePart().getCategory().getSection().getId())
                .sectionName(part.getVehiclePart().getCategory().getSection().getName())
                .isAccessory(part.getVehiclePart().getCategory().getIsAccessory())
                .build();
    }

    public static List<PartHistoryResponse> toServicePartHistoryResponses(Collection<ServiceRequestPart> requestParts) {
        return requestParts.stream()
                .map(PartConverter::toServicePartHistoryResponse)
                .collect(Collectors.toList());
    }
}
