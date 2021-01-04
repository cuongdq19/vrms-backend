package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.constants.MaintenanceConstants;
import org.lordrose.vrms.domains.MaintenancePackage;
import org.lordrose.vrms.domains.PartSection;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceVehiclePart;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.models.requests.MaintenancePackageRequest;
import org.lordrose.vrms.repositories.MaintenancePackageRepository;
import org.lordrose.vrms.repositories.PartSectionRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.FeedbackService;
import org.lordrose.vrms.services.MaintenancePackageService;
import org.lordrose.vrms.utils.distances.GeoPoint;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.MaintenancePackageConverter.toMaintenancePackageResponse;
import static org.lordrose.vrms.converters.MaintenancePackageConverter.toMaintenancePackageResponses;
import static org.lordrose.vrms.converters.MaintenancePackageConverter.toPackageProviderResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithIds;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class MaintenancePackageServiceImpl implements MaintenancePackageService {

    private final MaintenancePackageRepository packageRepository;
    private final PartSectionRepository sectionRepository;
    private final ServiceRepository serviceRepository;
    private final VehicleModelRepository modelRepository;

    private final FeedbackService feedbackService;
    private final MaintenanceConstants.MaintenanceMilestone milestone;

    @Override
    public Object findAllByProviderId(Long providerId) {
        return toMaintenancePackageResponses(
                packageRepository.findDistinctByPackagedServices_Provider_Id(providerId));
    }

    @Override
    public Object findById(Long packageId) {
        return toMaintenancePackageResponse(packageRepository.findById(packageId)
                .orElseThrow(() -> newExceptionWithId(packageId)));
    }

    @Transactional
    @Override
    public Object create(Long providerId, MaintenancePackageRequest request) {
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
        List<Long> serviceIds = request.getServiceIds();
        List<Service> services = serviceRepository.findAllByProviderIdAndIdIsIn(providerId, serviceIds);

        validateRetrievedServices(services, serviceIds);

        MaintenancePackage saved = packageRepository.save(MaintenancePackage.builder()
                .name(request.getPackageName())
                .milestone(milestone)
                .section(section)
                .packagedServices(new LinkedHashSet<>(services))
                .build());
        return toMaintenancePackageResponse(saved);
    }

    @Override
    public Object update(Long packageId, MaintenancePackageRequest request) {
        PartSection section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> newExceptionWithId(request.getSectionId()));
        MaintenancePackage result = packageRepository.findById(packageId)
                .orElseThrow(() -> newExceptionWithId(packageId));
        List<Long> serviceIds = request.getServiceIds();
        List<Service> services = serviceRepository.findAllById(serviceIds);

        validateRetrievedServices(services, serviceIds);
        result.getPackagedServices().clear();

        result.setName(request.getPackageName());
        result.setMilestone(milestone.getMilestoneAt(request.getMilestoneId()));
        result.setSection(section);
        result.setPackagedServices(new LinkedHashSet<>(services));

        MaintenancePackage saved = packageRepository.save(result);

        return toMaintenancePackageResponse(saved);
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

    @Transactional
    @Override
    public Object findAllByProviderIdAndModelId(Long providerId, Long modelId) {
        List<MaintenancePackage> packages =
                packageRepository.findDistinctByPackagedServices_Provider_Id(providerId);
        VehicleModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> newExceptionWithId(modelId));

        List<MaintenancePackage> results = packages.stream().filter(maintenancePackage -> {
            Set<Service> services = maintenancePackage.getPackagedServices();
            long count = services.stream().filter(service -> {
                if (service.getModels() != null && !service.getModels().isEmpty()) {
                    return service.getModels().contains(model);
                } else if (service.getPartSet() != null) {
                    Set<ServiceVehiclePart> serviceParts = service.getPartSet();
                    for (ServiceVehiclePart servicePart : serviceParts) {
                        if (servicePart.getPart().getModels().contains(model)) {
                            return true;
                        }
                    }
                }
                return false;
            }).count();
            return count == services.size();
        }).collect(Collectors.toList());
        return toMaintenancePackageResponses(results);
    }

    @Transactional
    @Override
    public Object findAllBySectionIdAndModelId(Long sectionId, Long modelId, GeoPoint currentLocation) {
        PartSection section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> newExceptionWithId(sectionId));
        VehicleModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> newExceptionWithId(modelId));
        List<MaintenancePackage> packages =
                packageRepository.findAllBySectionId(section.getId());

        List<MaintenancePackage> results = packages.stream()
                .filter(maintenancePackage -> {
                    Set<Service> services = maintenancePackage.getPackagedServices();
                    long count = services.stream()
                            .filter(service -> {
                                if (service.getModels() != null && !service.getModels().isEmpty()) {
                                    return service.getModels().contains(model);
                                } else if (service.getPartSet() != null) {
                                    Set<ServiceVehiclePart> serviceParts = service.getPartSet();
                                    for (ServiceVehiclePart servicePart : serviceParts) {
                                        if (servicePart.getPart().getModels().contains(model)) {
                                            return true;
                                        }
                                    }
                                }
                                return false;})
                            .count();
                    return count == services.size();})
                .collect(Collectors.toList());

        return toPackageProviderResponses(results, currentLocation, feedbackService);
    }

    @Transactional
    @Override
    public Object findAllByMilestoneIdAndModelId(Integer milestoneId, Long modelId, GeoPoint currentLocation) {
        VehicleModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> newExceptionWithId(modelId));
        List<MaintenancePackage> packages =
                packageRepository.findAllByMilestoneEquals(milestone.getMilestoneAt(milestoneId));

        List<MaintenancePackage> results = packages.stream()
                .filter(maintenancePackage -> {
                    Set<Service> services = maintenancePackage.getPackagedServices();
                    long count = services.stream()
                            .filter(service -> {
                                if (service.getModels() != null && !service.getModels().isEmpty()) {
                                    return service.getModels().contains(model);
                                } else if (service.getPartSet() != null) {
                                    Set<ServiceVehiclePart> serviceParts = service.getPartSet();
                                    for (ServiceVehiclePart servicePart : serviceParts) {
                                        if (servicePart.getPart().getModels().contains(model)) {
                                            return true;
                                        }
                                    }
                                }
                                return false;})
                            .count();
                    return count == services.size();})
                .collect(Collectors.toList());

        return toPackageProviderResponses(results, currentLocation, feedbackService);
    }
}
