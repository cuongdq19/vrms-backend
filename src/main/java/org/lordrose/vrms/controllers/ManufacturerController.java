package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.responses.ManufacturerResponse;
import org.lordrose.vrms.services.ManufacturerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @GetMapping
    public List<ManufacturerResponse> getAllManufacturers() {
        return manufacturerService.findAll();
    }

    @PostMapping
    public ManufacturerResponse createManufacturer(@RequestBody String manufacturerName) {
        return manufacturerService.create(manufacturerName);
    }
}
