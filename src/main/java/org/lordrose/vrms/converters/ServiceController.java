package org.lordrose.vrms.converters;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.ServiceRequest;
import org.lordrose.vrms.models.responses.ServiceResponse;
import org.lordrose.vrms.services.ServiceProcessingService;
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
@RequestMapping("/services")
public class ServiceController {

    private final ServiceProcessingService processingService;

    @GetMapping
    public List<ServiceResponse> getAllServices() {
        return processingService.findAll();
    }

    @PostMapping
    public ServiceResponse createNew(@RequestBody ServiceRequest request) {
        return processingService.create(request);
    }

    @PostMapping("/{id}")
    public ServiceResponse updateService(@PathVariable Long id,
                                         @RequestBody ServiceRequest request) {
        return processingService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ServiceResponse deleteService(@PathVariable Long id) {
        return processingService.delete(id);
    }
}
