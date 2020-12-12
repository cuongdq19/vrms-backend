package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.services.ServiceProcessingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceProcessingService processingService;

    @GetMapping("/providers/{providerId}")
    public Object findAllServicesByProviderId(@PathVariable Long providerId) {
        return processingService.findAllByProviderId(providerId);
    }

    @PostMapping("/providers/{providerId}")
    public Object createService(@PathVariable Long providerId,
                                @RequestBody ServiceInfoRequest request) {
        return processingService.create(providerId, request);
    }
}
