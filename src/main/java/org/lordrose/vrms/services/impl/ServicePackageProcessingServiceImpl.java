package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.PartSection;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServicePackage;
import org.lordrose.vrms.models.requests.ServicePackageRequest;
import org.lordrose.vrms.repositories.PartSectionRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.ServicePackageRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.services.ServicePackageProcessingService;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ServicePackageConverter.toServicePackageResponse;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithIds;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServicePackageProcessingServiceImpl implements ServicePackageProcessingService {

    private final ServicePackageRepository packageRepository;
    private final PartSectionRepository sectionRepository;
    private final ServiceRepository serviceRepository;
    private final ProviderRepository providerRepository;

    @Override
    public Object create(Long providerId, ServicePackageRequest request) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));
        PartSection section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> newExceptionWithId(request.getSectionId()));
        Set<Long> serviceIds = request.getServiceIds();
        List<Service> services = serviceRepository.findAllById(serviceIds);

        validateRetrievedServices(services, serviceIds);

        ServicePackage saved = packageRepository.save(ServicePackage.builder()
                .name(request.getPackageName())
                .milestone(request.getMilestone())
                .section(section)
                .packagedServices(new LinkedHashSet<>(services))
                .provider(provider)
                .build());
        return toServicePackageResponse(saved);
    }

    @Override
    public Object update(Long packageId, ServicePackageRequest request) {
        PartSection section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> newExceptionWithId(request.getSectionId()));
        ServicePackage result = packageRepository.findById(packageId)
                .orElseThrow(() -> newExceptionWithId(packageId));
        Set<Long> serviceIds = request.getServiceIds();
        List<Service> services = serviceRepository.findAllById(serviceIds);

        validateRetrievedServices(services, serviceIds);
        result.getPackagedServices().clear();

        result.setName(request.getPackageName());
        result.setMilestone(request.getMilestone());
        result.setSection(section);
        result.setPackagedServices(new LinkedHashSet<>(services));

        ServicePackage saved = packageRepository.save(result);

        return toServicePackageResponse(saved);
    }

    @Override
    public void delete(Long packageId) {
        ServicePackage result = packageRepository.findById(packageId)
                .orElseThrow(() -> newExceptionWithId(packageId));

        packageRepository.delete(result);
    }

    private void validateRetrievedServices(Collection<Service> services, Collection<Long> serviceIds) {
        if (services.size() != serviceIds.size()) {
            List<Long> tempList = services.stream()
                    .map(Service::getId)
                    .collect(Collectors.toList());
            List<Long> notFounds = serviceIds.stream()
                    .filter(test -> !tempList.contains(test))
                    .collect(Collectors.toList());
            throw newExceptionWithIds(notFounds);
        }
    }
}
