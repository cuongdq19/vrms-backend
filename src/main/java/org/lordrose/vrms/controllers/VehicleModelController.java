package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.VehicleModelRequest;
import org.lordrose.vrms.models.responses.VehicleModelResponse;
import org.lordrose.vrms.services.VehicleModelService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/models")
public class VehicleModelController {

    private final VehicleModelService modelService;

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
}
