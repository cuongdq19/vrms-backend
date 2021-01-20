package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.User;
import org.lordrose.vrms.domains.Vehicle;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.models.requests.VehicleRequest;
import org.lordrose.vrms.models.responses.VehicleResponse;
import org.lordrose.vrms.repositories.UserRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.repositories.VehicleRepository;
import org.lordrose.vrms.services.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return toVehicleResponses(vehicleRepository.findAllByUserId(id).stream()
                .filter(vehicle -> !vehicle.getIsDeleted())
                .collect(Collectors.toList()));
    }

    @Override
    public VehicleResponse findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id));

        if (vehicle.getIsDeleted()) {
            throw newExceptionWithId(id);
        }

        return toVehicleResponse(vehicle);
    }

    @Override
    public VehicleResponse create(VehicleRequest request) {
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId()));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> newExceptionWithId(request.getUserId()));
        vehicleRepository.findByVinNumberIgnoreCase(request.getVinNumber())
                .ifPresent(existedVehicle -> {
                    throw new InvalidArgumentException("VIN is already existed");});
        vehicleRepository.findByPlateNumberIgnoreCase(request.getPlateNumber())
                .ifPresent(existedVehicle -> {
                    throw new InvalidArgumentException("Plate number is already existed");});
        Vehicle saved = vehicleRepository.save(Vehicle.builder()
                .plateNumber(request.getPlateNumber())
                .vinNumber(request.getVinNumber())
                .color(request.getColor())
                .boughtDate(toLocalDateTime(request.getBoughtDate()))
                .isDeleted(false)
                .model(model)
                .user(user)
                .build());
        return toVehicleResponse(saved);
    }

    @Override
    public VehicleResponse delete(Long id) {
        Vehicle result = vehicleRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id));

        result.setIsDeleted(true);

        return toVehicleResponse(vehicleRepository.save(result));
    }

    @Override
    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle result = vehicleRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id));
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId()));

        result.setPlateNumber(request.getPlateNumber());
        result.setVinNumber(request.getVinNumber());
        result.setColor(request.getColor());
        result.setBoughtDate(toLocalDateTime(request.getBoughtDate()));
        result.setModel(model);

        return toVehicleResponse(vehicleRepository.save(result));
    }
}
