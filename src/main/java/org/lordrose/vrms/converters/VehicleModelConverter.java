package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.models.responses.VehicleModelResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleModelConverter {

    public static VehicleModelResponse toModelResponse(VehicleModel model) {
        return VehicleModelResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .year(model.getYear())
                .fuelType(model.getFuelType())
                .gearbox(model.getGearbox())
                .imageUrl(model.getImageUrl())
                .manufacturerId(model.getManufacturer().getId())
                .manufacturerName(model.getManufacturer().getName())
                .build();
    }

    public static List<VehicleModelResponse> toModelResponses(Collection<VehicleModel> models) {
        return models.stream()
                .map(VehicleModelConverter::toModelResponse)
                .collect(Collectors.toList());
    }
}
