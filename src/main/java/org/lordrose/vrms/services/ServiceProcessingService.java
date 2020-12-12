package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.ServiceInfoRequest;

public interface ServiceProcessingService {

    Object findAllByProviderId(Long providerId);

    Object create(Long providerId, ServiceInfoRequest request);
}
