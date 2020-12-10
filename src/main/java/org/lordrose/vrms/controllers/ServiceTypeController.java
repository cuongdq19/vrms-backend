package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.services.ServiceTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service-types")
public class ServiceTypeController {

    private final ServiceTypeService typeService;

    @GetMapping
    public Object getAllType() {
        return typeService.findAll();
    }
}
