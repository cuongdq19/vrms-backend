package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.MaintenancePackageRequest;

public interface MaintenancePackageService {

    Object findAllByProviderId(Long providerId);

    Object findById(Long packageId);

    Object create(Long providerId, MaintenancePackageRequest request);

    Object update(Long packageId, MaintenancePackageRequest request);

    void delete(Long packageId);
}
