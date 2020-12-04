package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.models.requests.VehicleRequest;
import org.lordrose.vrms.models.responses.VehicleResponse;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.repositories.VehicleRepository;
import org.lordrose.vrms.services.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.lordrose.vrms.converters.VehicleConverter.toVehicleResponse;
import static org.lordrose.vrms.converters.VehicleConverter.toVehicleResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.utils.DateTimeUtils.toLocalDateTime;

@RequiredArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleModelRepository modelRepository;
    private final UserRepository userRepository;

    @Override
    public List<VehicleResponse> findAllByUserId(Long id) {
        return toVehicleResponses(vehicleRepository.findAllByUserId(id));
    }

    @Override
    public VehicleResponse findById(Long id) {
        return toVehicleResponse(vehicleRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id.toString())));
    }

    @Override
    public VehicleResponse create(VehicleRequest request) {
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId().toString()));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> newExceptionWithId(request.getUserId().toString()));
        Vehicle saved = vehicleRepository.save(Vehicle.builder()
                .plateNumber(request.getPlateNumber())
                .vinNumber(request.getVinNumber())
                .color(request.getColor())
                .boughtDate(toLocalDateTime(request.getBoughtDate()))
                .model(model)
                .user(user)
                .build());
        return toVehicleResponse(saved);
    }

    @Override
    public VehicleResponse delete(Long id) {
        Vehicle result = vehicleRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id.toString()));
        vehicleRepository.deleteById(result.getId());
        return toVehicleResponse(result);
    }

    @Override
    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle result = vehicleRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id.toString()));
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId().toString()));

        result.setPlateNumber(request.getPlateNumber());
        result.setVinNumber(request.getVinNumber());
        result.setColor(request.getColor());
        result.setBoughtDate(toLocalDateTime(request.getBoughtDate()));
        result.setModel(model);

        return toVehicleResponse(vehicleRepository.save(result));
    }
}
