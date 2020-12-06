package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.models.requests.ServiceRequest;
import org.lordrose.vrms.models.responses.ServiceResponse;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.services.ServiceProcessingService;

import java.util.List;

import static org.lordrose.vrms.converters.ServiceConverter.toServiceResponse;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;

@RequiredArgsConstructor

public class ServiceProcessingServiceImpl implements ServiceProcessingService {

    private final ServiceRepository serviceRepository;

    @Override
    public List<ServiceResponse> findAll() {
        return toServiceResponses(serviceRepository.findAll());
    }

    @Override
    public ServiceResponse create(ServiceRequest request) {
        Service saved = serviceRepository.save(Service.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .imageUrl("image url")
                .build());
        return toServiceResponse(saved);
    }

    @Override
    public ServiceResponse update(Long id, ServiceRequest request) {
        Service result = serviceRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id.toString()));

        result.setName(request.getName());
        result.setType(request.getType());
        result.setDescription(request.getDescription());

        return toServiceResponse(serviceRepository.save(result));
    }

    @Override
    public ServiceResponse delete(Long id) {
        Service result = serviceRepository.findById(id)
                .orElseThrow(() -> newExceptionWithId(id.toString()));

        serviceRepository.deleteById(id);

        return toServiceResponse(result);
    }
}
