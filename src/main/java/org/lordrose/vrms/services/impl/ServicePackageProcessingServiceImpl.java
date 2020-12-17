package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.BasePackage;
import org.lordrose.vrms.domains.ServicePackage;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.models.responses.BaseResponse;
import org.lordrose.vrms.repositories.BasePackageRepository;
import org.lordrose.vrms.repositories.ServicePackageRepository;
import org.lordrose.vrms.services.ServicePackageProcessingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        Map<BasePackage, List<ServiceTypeDetail>> tempMap = byBase.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .flatMap(value -> value.getBasePackage().getTypeDetails().stream())
                                .collect(Collectors.toList())));

        List<BaseResponse> responses = new ArrayList<>();

        /*tempMap.forEach((base, servicePackage) -> responses.add(BaseResponse.builder()
                .name(base.getName())
                .description(base.getDescription())
                .services(servicePackage.stream()
                        .map(temp -> temp.getPartCategoryId()))
                .build()));*/

        return responses;
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
