package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.repositories.ServicePackageRepository;
import org.lordrose.vrms.services.ServicePackageProcessingService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ServicePackageProcessingServiceImpl implements ServicePackageProcessingService {

    private final ServicePackageRepository packageRepository;

    @Override
    public Object getBases(Long providerId) {
        return null;
    }

    @Override
    public Object getBases(Long baseId, Long providerId) {
        return null;
    }

    @Override
    public Object create(Long providerId) {
        return null;
    }

    @Override
    public Object update(Long packageId) {
        return null;
    }

    @Override
    public void delete(Long packageId) {

    }
}
