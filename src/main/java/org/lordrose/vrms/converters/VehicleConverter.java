package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.models.responses.VehicleResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.utils.DateTimeUtils.toSeconds;

public class VehicleConverter {

    public static VehicleResponse toVehicleResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .plateNumber(vehicle.getPlateNumber())
                .vinNumber(vehicle.getVinNumber())
                .color(vehicle.getColor())
                .boughtDate(toSeconds(vehicle.getBoughtDate()))
                .modelId(vehicle.getModel().getId())
                .modelName(vehicle.getModel().getName())
                .manufacturerId(vehicle.getModel().getManufacturer().getId())
                .manufacturerName(vehicle.getModel().getManufacturer().getName())
                .build();
    }

    public static List<VehicleResponse> toVehicleResponses(Collection<Vehicle> vehicles) {
        return vehicles.stream()
                .map(VehicleConverter::toVehicleResponse)
                .collect(Collectors.toList());
    }
}
