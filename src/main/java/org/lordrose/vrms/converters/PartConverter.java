package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.RequestPart;
import org.lordrose.vrms.models.responses.PartResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PartConverter {

    public static PartResponse toPartResponse(RequestPart part) {
        return PartResponse.builder().build();
    }

    public static List<PartResponse> toPartResponses(Collection<RequestPart> parts) {
        return parts.stream()
                .map(PartConverter::toPartResponse)
                .collect(Collectors.toList());
    }
}
