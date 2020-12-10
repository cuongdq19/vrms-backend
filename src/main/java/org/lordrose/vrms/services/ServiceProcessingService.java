package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.models.requests.ServiceRequest;
import org.lordrose.vrms.models.responses.ServiceResponse;

import java.util.List;

public interface ServiceProcessingService {

    List<ServiceResponse> findAll();

    ServiceResponse create(ServiceRequest request);

    ServiceResponse update(Long id, ServiceRequest request);

    ServiceResponse delete(Long id);

    Object create(Long providerId, ServiceInfoRequest request);
}
