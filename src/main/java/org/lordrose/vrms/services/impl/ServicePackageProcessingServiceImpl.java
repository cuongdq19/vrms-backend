package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.BasePackage;
import org.lordrose.vrms.domains.ServicePackage;
import org.lordrose.vrms.repositories.BasePackageRepository;
import org.lordrose.vrms.repositories.ServicePackageRepository;
import org.lordrose.vrms.services.ServicePackageProcessingService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ServicePackageProcessingServiceImpl implements ServicePackageProcessingService {

    private final BasePackageRepository baseRepository;
    private final ServicePackageRepository packageRepository;

    @Override
    public Object getBases(Long providerId) {
        List<ServicePackage> packages = packageRepository.findAllByProviderId(providerId);

        Map<BasePackage, List<ServicePackage>> byBase = new LinkedHashMap<>();
        baseRepository.findAll().forEach(base -> byBase.put(base, Collections.emptyList()));
        byBase.putAll(packages.stream()
                .collect(Collectors.groupingBy(ServicePackage::getBasePackage)));

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
