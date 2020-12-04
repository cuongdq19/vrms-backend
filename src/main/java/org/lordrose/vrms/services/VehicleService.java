package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.VehicleRequest;
import org.lordrose.vrms.models.responses.VehicleResponse;

import java.util.List;

public interface VehicleService {

    List<VehicleResponse> findAllByUserId(Long id);

    VehicleResponse findById(Long id);

    VehicleResponse create(VehicleRequest request);

    VehicleResponse delete(Long id);

    VehicleResponse update(Long id, VehicleRequest request);
}
