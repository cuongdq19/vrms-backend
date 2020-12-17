package org.lordrose.vrms.services;

public interface ServicePackageProcessingService {

    Object getBases(Long providerId);

    Object create(Long providerId);

    Object update(Long packageId);

    void delete(Long packageId);
}
