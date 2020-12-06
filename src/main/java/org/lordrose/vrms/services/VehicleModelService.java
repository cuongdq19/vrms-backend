package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.VehicleModelRequest;
import org.lordrose.vrms.models.responses.VehicleModelResponse;

import java.util.List;

public interface VehicleModelService {

    List<VehicleModelResponse> findAll();

    List<VehicleModelResponse> findAllByManufacturerId(Long id);

    VehicleModelResponse create(VehicleModelRequest request);

    VehicleModelResponse update(Long id, VehicleModelRequest request);

    VehicleModelResponse delete(Long id);
}
