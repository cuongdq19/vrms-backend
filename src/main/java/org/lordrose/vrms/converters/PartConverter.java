package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.PartRequest;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.responses.PartResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PartConverter {

    public static PartResponse toPartResponse(VehiclePart part) {
        return PartResponse.builder()
                .build();
    }

    public static List<PartResponse> toPartResponses(Collection<VehiclePart> parts) {
        return parts.stream()
                .map(PartConverter::toPartResponse)
                .collect(Collectors.toList());
    }

    public static PartResponse toPartDetailResponse(PartRequest part) {
        return PartResponse.builder().build();
    }

    public static List<PartResponse> toPartDetailResponses(Collection<PartRequest> parts) {
        return parts.stream()
                .map(PartConverter::toPartDetailResponse)
                .collect(Collectors.toList());
    }
}
