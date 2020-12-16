package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Manufacturer;
import org.lordrose.vrms.models.responses.ManufacturerResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ManufacturerConverter {

    public static ManufacturerResponse toManufacturerResponse(Manufacturer manufacturer) {
        return ManufacturerResponse.builder()
                .id(manufacturer.getId())
                .name(manufacturer.getName())
                .imageUrl(manufacturer.getImageUrl())
                .build();
    }

    public static List<ManufacturerResponse> toManufacturerResponses(Collection<Manufacturer> manufacturers) {
        return manufacturers.stream()
                .map(ManufacturerConverter::toManufacturerResponse)
                .collect(Collectors.toList());
    }
}
