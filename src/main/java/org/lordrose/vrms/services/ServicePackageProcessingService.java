package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.ServicePackageRequest;

public interface ServicePackageProcessingService {

    Object create(Long providerId, ServicePackageRequest request);

    Object update(Long packageId, ServicePackageRequest request);

    void delete(Long packageId);
}
