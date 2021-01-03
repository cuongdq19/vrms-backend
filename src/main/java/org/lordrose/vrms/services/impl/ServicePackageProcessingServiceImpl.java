package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.constants.MaintenanceConstants;
import org.lordrose.vrms.domains.MaintenancePackage;
import org.lordrose.vrms.domains.PartSection;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
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
import static org.lordrose.vrms.converters.ServicePackageConverter.toServicePackageResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithIds;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServicePackageProcessingServiceImpl implements ServicePackageProcessingService {

    private final ServicePackageRepository packageRepository;
    private final PartSectionRepository sectionRepository;
    private final ServiceRepository serviceRepository;
    private final ProviderRepository providerRepository;

    private final MaintenanceConstants.MaintenanceMilestone milestone;

    @Override
    public Object findAllByProviderId(Long providerId) {
        return toServicePackageResponses(
                packageRepository.findDistinctByPackagedServices_Provider_Id(providerId));
    }

    @Override
    public Object findById(Long packageId) {
        return toServicePackageResponse(packageRepository.findById(packageId)
                .orElseThrow(() -> newExceptionWithId(packageId)));
    }

    @Override
    public Object create(Long providerId, ServicePackageRequest request) {
        PartSection section = null;
        Double milestone = null;

        if (request.getSectionId() == null) {
            if (request.getMilestoneId() == null) {
                throw new InvalidArgumentException("SectionId and MilestoneId mustn't both be null");
            } else {
                milestone = this.milestone.getMilestoneAt(request.getMilestoneId());
            }
        } else if (request.getMilestoneId() != null) {
            throw new InvalidArgumentException("Section ID and Milestone mustn't both be present");
        } else {
            section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> newExceptionWithId(request.getSectionId()));
        }
        Set<Long> serviceIds = request.getServiceIds();
        List<Service> services = serviceRepository.findAllByProviderIdAndIdIsIn(providerId, serviceIds);

        validateRetrievedServices(services, serviceIds);

        MaintenancePackage saved = packageRepository.save(MaintenancePackage.builder()
                .name(request.getPackageName())
                .milestone(milestone)
                .section(section)
                .packagedServices(new LinkedHashSet<>(services))
                .build());
        return toServicePackageResponse(saved);
    }

    @Override
    public Object update(Long packageId, ServicePackageRequest request) {
        PartSection section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> newExceptionWithId(request.getSectionId()));
        MaintenancePackage result = packageRepository.findById(packageId)
                .orElseThrow(() -> newExceptionWithId(packageId));
        Set<Long> serviceIds = request.getServiceIds();
        List<Service> services = serviceRepository.findAllById(serviceIds);

        validateRetrievedServices(services, serviceIds);
        result.getPackagedServices().clear();

        result.setName(request.getPackageName());
        result.setMilestone(milestone.getMilestoneAt(request.getMilestoneId()));
        result.setSection(section);
        result.setPackagedServices(new LinkedHashSet<>(services));

        MaintenancePackage saved = packageRepository.save(result);

        return toServicePackageResponse(saved);
    }

    @Override
    public void delete(Long packageId) {
        MaintenancePackage result = packageRepository.findById(packageId)
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
