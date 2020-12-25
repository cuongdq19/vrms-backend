package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceType;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.domains.ServiceVehiclePart;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.requests.GroupPriceRequest;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.models.responses.ServiceOptionResponse;
import org.lordrose.vrms.repositories.ModelGroupRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.repositories.ServiceTypeRepository;
import org.lordrose.vrms.repositories.ServiceVehiclePartRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.repositories.VehiclePartRepository;
import org.lordrose.vrms.services.ServiceProcessingService;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ServiceConverter.toAllServicesResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceOptionResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceResponse;
import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithIds;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceProcessingServiceImpl implements ServiceProcessingService {

    private final ServiceRepository serviceRepository;
    private final ServiceTypeDetailRepository typeDetailRepository;
    private final ServiceTypeRepository typeRepository;
    private final ProviderRepository providerRepository;
    private final ModelGroupRepository groupRepository;
    private final VehicleModelRepository modelRepository;
    private final VehiclePartRepository partRepository;
    private final ServiceVehiclePartRepository servicePartRepository;

    @Transactional
    @Override
    public Object findAllByProviderId(Long providerId) {
        return toAllServicesResponses(
                serviceRepository.findAllByProviderId(providerId));
    }

    @Transactional
    @Override
    public Object findAllByProviderIdAndTypeId(Long providerId, Long typeId) {
        ServiceType type = typeRepository.findById(typeId)
                .orElseThrow(() -> newExceptionWithId(typeId));
        return toAllServicesResponses(
                serviceRepository.findAllByProviderIdAndTypeDetailType(providerId, type));
    }

    @Override
    public Object findAllByProviderIdAndModelIdAndPartIds(Long providerId, Long modelId,
                                                          Set<Long> partIds) {
        Map<Long, List<ServiceOptionResponse>> servicePartIds = new LinkedHashMap<>();

        partIds.forEach(partId -> {
            Set<Service> services = new LinkedHashSet<>();
            if (!services.isEmpty())
                servicePartIds.put(partId, toServiceOptionResponses(services));
        });

        return servicePartIds;
    }

    private void validateCreate(Long detailId, Long providerId, Set<Long> modelIds) {
        /*List<Service> services = serviceRepository.findAllByTypeDetailIdAndProviderId(
                detailId, providerId);

        Set<Long> createdModelIds = services.stream()
                .flatMap(service -> service.getModelGroup().getModels().stream())
                .map(VehicleModel::getId)
                .collect(Collectors.toSet());

        if (CollectionUtils.intersection(modelIds, createdModelIds).size() != 0) {
            throw new InvalidArgumentException("Only one VehicleModel can existed in one ServiceTypeDetail");
        }*/
    }

    @Transactional
    @Override
    public Object create(Long providerId, ServiceInfoRequest request) {
        ServiceTypeDetail typeDetail = typeDetailRepository.findById(request.getTypeDetailId())
                .orElseThrow(() -> newExceptionWithId(request.getTypeDetailId()));
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));
        Set<Long> partIds = request.getGroupPriceRequest().getPartIds();

        validateCreate(request.getTypeDetailId(), providerId, request.getGroupPriceRequest().getModelIds());

        List<VehiclePart> parts = partRepository.findAllById(partIds);
        if (parts.size() != partIds.size()) {
            List<Long> retrievedIds = parts.stream()
                    .map(VehiclePart::getId)
                    .collect(Collectors.toList());
            List<Long> notFounds = partIds.stream()
                    .filter(partId -> !retrievedIds.contains(partId))
                    .collect(Collectors.toList());
            throw newExceptionWithIds(notFounds);
        }

        Service service = serviceRepository.save(Service.builder()
                .name(request.getGroupPriceRequest().getName())
                .price(request.getGroupPriceRequest().getPrice())
                .typeDetail(typeDetail)
                .provider(provider)
                .build());

        Set<ServiceVehiclePart> list = parts.stream()
                .map(part -> ServiceVehiclePart.builder()
                        .part(part)
                        .quantity(2.5)
                        .service(service)
                        .build())
                .collect(Collectors.toSet());
        service.setPartSet(list);

        return toServiceResponse(service);
    }

    private void validateUpdate(Service updatingService, Set<Long> modelIds) {
        /*List<Service> services = serviceRepository.findAllByTypeDetailIdAndProviderId(
                updatingService.getTypeDetail().getId(), updatingService.getProvider().getId());
        services.remove(updatingService);

        Set<Long> createdModelIds = services.stream()
                .flatMap(service -> service.getModelGroup().getModels().stream())
                .map(VehicleModel::getId)
                .collect(Collectors.toSet());

        if (CollectionUtils.intersection(modelIds, createdModelIds).size() != 0) {
            throw new InvalidArgumentException("Only one VehicleModel can existed in one ServiceTypeDetail");
        }*/
    }

    @Transactional
    @Override
    public Object update(Long serviceId, GroupPriceRequest request) {
        Service result = serviceRepository.findById(serviceId)
                .orElseThrow(() -> newExceptionWithId(serviceId));

        validateUpdate(result, request.getModelIds());

        Set<Long> partIds = request.getPartIds();
        List<VehiclePart> parts = partRepository.findAllById(partIds);
        if (parts.size() != partIds.size()) {
            List<Long> retrievedIds = parts.stream()
                    .map(VehiclePart::getId)
                    .collect(Collectors.toList());
            List<Long> notFounds = partIds.stream()
                    .filter(partId -> !retrievedIds.contains(partId))
                    .collect(Collectors.toList());
            throw newExceptionWithIds(notFounds);
        }

        Set<ServiceVehiclePart> set = parts.stream()
                .map(part -> ServiceVehiclePart.builder()
                        .part(part)
                        .quantity(2.5)
                        .service(result)
                        .build())
                .collect(Collectors.toSet());

        result.setName(request.getName());
        result.setPrice(request.getPrice());
        result.getPartSet().clear();
        result.setPartSet(set);

        return toServiceResponse(serviceRepository.save(result));
    }

    @Override
    public void delete(Long serviceId) {
        serviceRepository.findById(serviceId)
                .ifPresentOrElse(
                        service -> serviceRepository.deleteById(service.getId()),
                        () -> {throw newExceptionWithId(serviceId);});
    }

    @Override
    public Object findAllModels(Long detailId, Long providerId) {
        List<Service> services = serviceRepository.findAllByTypeDetailIdAndProviderId(
                detailId, providerId);

        Set<Long> modelIds = new LinkedHashSet<>();

        if (!modelIds.isEmpty())
            return toModelResponses(modelRepository.findAllByIdNotIn(modelIds));
        else
            return toModelResponses(modelRepository.findAll());
    }
}
