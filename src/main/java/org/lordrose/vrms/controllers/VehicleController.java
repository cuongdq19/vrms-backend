package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.VehicleRequest;
import org.lordrose.vrms.models.responses.VehicleResponse;
import org.lordrose.vrms.services.VehicleService;
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
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/users/{id}")
    public List<VehicleResponse> getAllVehicleByUserId(@PathVariable Long id) {
        return vehicleService.findAllByUserId(id);
    }

    @GetMapping("{id}")
    public VehicleResponse findById(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @PostMapping
    public VehicleResponse createNewVehicle(@RequestBody VehicleRequest request) {
        return vehicleService.create(request);
    }

    @DeleteMapping("/{id}")
    public VehicleResponse deleteVehicle(@PathVariable Long id) {
        return vehicleService.delete(id);
    }

    @PostMapping("/{id}")
    public VehicleResponse updateVehicle(@PathVariable Long id,
                                         @RequestBody VehicleRequest request) {
        return vehicleService.update(id, request);
    }

    /*@GetMapping("/{vehicleId}/maintenances")
    public List<UserReminderResponse> getUserReminder(@PathVariable Long vehicleId) {
        return reminderService.getReminders(vehicleId);
    }*/
}
