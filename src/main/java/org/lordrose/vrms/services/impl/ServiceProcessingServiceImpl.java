package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.ServiceRequest;
import org.lordrose.vrms.models.responses.ServiceResponse;
import org.lordrose.vrms.services.ServiceProcessingService;

import java.util.List;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceProcessingServiceImpl implements ServiceProcessingService {

    @Override
    public List<ServiceResponse> findAll() {
        return null;
    }

    @Override
    public ServiceResponse create(ServiceRequest request) {
        return null;
    }

    @Override
    public ServiceResponse update(Long id, ServiceRequest request) {
        return null;
    }

    @Override
    public ServiceResponse delete(Long id) {
        return null;
    }
}
