package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Request;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.models.responses.VehicleRequestInfoResponse;
import org.lordrose.vrms.models.responses.VehicleResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.RequestConverter.toRequestHistoryResponse;
import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponse;
import static org.lordrose.vrms.utils.DateTimeUtils.toSeconds;

public class VehicleConverter {

    private final String abc = "abc";

    public static VehicleResponse toVehicleResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .plateNumber(vehicle.getPlateNumber())
                .vinNumber(vehicle.getVinNumber())
                .color(vehicle.getColor())
                .boughtDate(toSeconds(vehicle.getBoughtDate()))
                .model(toModelResponse(vehicle.getModel()))
                .build();
    }

    public static List<VehicleResponse> toVehicleResponses(Collection<Vehicle> vehicles) {
        return vehicles.stream()
                .map(VehicleConverter::toVehicleResponse)
                .collect(Collectors.toList());
    }

    public static VehicleRequestInfoResponse toVehicleRequestInfoResponse(Vehicle vehicle,
                                                                          Request request) {
        return VehicleRequestInfoResponse.builder()
                .vehicle(toVehicleResponse(vehicle))
                .request(toRequestHistoryResponse(request))
                .build();
    }

    public static List<VehicleRequestInfoResponse> toVehicleRequestInfoResponses(Map<Vehicle, Optional<Request>> vehicles) {
        List<VehicleRequestInfoResponse> responses = new ArrayList<>();
        vehicles.forEach((vehicle, request) ->
                responses.add(
                        toVehicleRequestInfoResponse(vehicle, request.orElse(null))));
        return responses;
    }
}
