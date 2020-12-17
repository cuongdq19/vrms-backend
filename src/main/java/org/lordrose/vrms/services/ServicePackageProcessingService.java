package org.lordrose.vrms.services;

public interface ServicePackageProcessingService {

    Object getBases(Long providerId);

    Object getBases(Long baseId, Long providerId);

    Object create(Long providerId);

    Object update(Long packageId);

    void delete(Long packageId);
}
