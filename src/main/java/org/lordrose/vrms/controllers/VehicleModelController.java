package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.VehicleModelQueryRequest;
import org.lordrose.vrms.models.requests.VehicleModelRequest;
import org.lordrose.vrms.models.responses.VehicleModelResponse;
import org.lordrose.vrms.repositories.ManufacturerRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.VehicleModelService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ManufacturerConverter.toManufacturerResponses;
import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponses;

@RequiredArgsConstructor
@RestController
@RequestMapping("/models")
public class VehicleModelController {

    private final VehicleModelService modelService;
    private final ManufacturerRepository manufacturerRepository;
    private final VehicleModelRepository modelRepository;

    @GetMapping
    public List<VehicleModelResponse> getAllVehicleModels() {
        return modelService.findAll();
    }

    @GetMapping("/manufacturers/{id}")
    public List<VehicleModelResponse> getAllVehicleModelsByManufacturerId(@PathVariable Long id) {
        return modelService.findAllByManufacturerId(id);
    }

    @PostMapping
    public VehicleModelResponse createNew(@RequestBody VehicleModelRequest request) {
        return modelService.create(request);
    }

    @PostMapping("/{id}")
    public VehicleModelResponse update(@PathVariable Long id,
                                      @RequestBody VehicleModelRequest request) {
        return modelService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public VehicleModelResponse delete(@PathVariable Long id) {
        return modelService.delete(id);
    }

    @PostMapping("/find")
    public Object findVehicleModel(@RequestBody VehicleModelQueryRequest request) {
        Long manufacturerId = request.getManufacturerId();
        String name = request.getModelName();
        if (manufacturerId != null) {
            if (!"".equals(name))
                return toModelResponses(modelRepository.findAllByManufacturerIdAndNameIgnoreCase(manufacturerId, name));
            return modelRepository.findDistinctByManufacturerId(manufacturerId).stream()
                    .map(model -> model.getName().toLowerCase()).distinct()
                    .collect(Collectors.toList());
        }
        return toManufacturerResponses(manufacturerRepository.findAll());
    }
}
