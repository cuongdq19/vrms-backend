package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Manufacturer;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.models.requests.VehicleModelRequest;
import org.lordrose.vrms.models.responses.VehicleModelResponse;
import org.lordrose.vrms.repositories.ManufacturerRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.VehicleModelService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponse;
import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;

@RequiredArgsConstructor
@Service
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository modelRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Override
    public List<VehicleModelResponse> findAll() {
        return toModelResponses(modelRepository.findAll());
    }

    @Override
    public List<VehicleModelResponse> findAllByManufacturerId(Long id) {
        return toModelResponses(modelRepository.findAllByManufacturerId(id));
    }

    @Override
    public VehicleModelResponse create(VehicleModelRequest request) {
        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                .orElseThrow(() -> newExceptionWithId(request.getManufacturerId().toString()));
        VehicleModel saved = modelRepository.save(VehicleModel.builder()
                .name(request.getName())
                .year(request.getYear())
                .fuelType(request.getFuelType())
                .gearbox(request.getGearbox())
                .imageUrl("image url")
                .manufacturer(manufacturer)
                .build());
        return toModelResponse(saved);
    }

    @Override
    public VehicleModelResponse update(Long id, VehicleModelRequest request) {
        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                .orElseThrow(() -> newExceptionWithId(request.getManufacturerId().toString()));
        VehicleModel result = modelRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id.toString()));

        result.setName(request.getName());
        result.setYear(request.getYear());
        result.setFuelType(request.getFuelType());
        result.setGearbox(request.getGearbox());
        result.setManufacturer(manufacturer);
        result.setImageUrl("image url updated");

        return toModelResponse(modelRepository.save(result));
    }

    @Override
    public VehicleModelResponse delete(Long id) {
        VehicleModel result = modelRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id.toString()));

        modelRepository.deleteById(id);

        return toModelResponse(result);
    }
}
