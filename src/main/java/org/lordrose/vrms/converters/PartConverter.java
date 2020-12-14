package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.PartRequest;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.responses.PartResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponses;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

public class PartConverter {

    public static PartResponse toPartResponse(VehiclePart part) {
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
                .models(toModelResponses(part.getModels()))
                .build();
    }

    public static List<PartResponse> toPartResponses(Collection<VehiclePart> parts) {
        return parts.stream()
                .map(PartConverter::toPartResponse)
                .collect(Collectors.toList());
    }

    public static PartResponse toPartDetailResponse(PartRequest part) {
        return PartResponse.builder()
                .build();
    }

    public static List<PartResponse> toPartDetailResponses(Collection<PartRequest> parts) {
        return parts.stream()
                .map(PartConverter::toPartDetailResponse)
                .collect(Collectors.toList());
    }
}
