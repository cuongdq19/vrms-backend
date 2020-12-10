package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.services.ServiceTypeDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service-type-details")
public class ServiceTypeDetailController {

    private final ServiceTypeDetailService typeDetailService;

    @GetMapping("/{typeId}")
    public Object findAllTypeId(@PathVariable Long typeId) {
        return typeDetailService.findAll(typeId);
    }
}
