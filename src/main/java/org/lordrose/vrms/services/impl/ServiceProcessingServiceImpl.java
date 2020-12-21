package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.lordrose.vrms.domains.ModelGroup;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceType;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.models.requests.GroupPriceRequest;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.models.responses.ServiceOptionResponse;
import org.lordrose.vrms.repositories.ModelGroupRepository;
import org.lordrose.vrms.repositories.PartRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.repositories.ServiceTypeRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.ServiceProcessingService;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ServiceConverter.toAllServicesResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceResponse;
import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceProcessingServiceImpl implements ServiceProcessingService {

    private final ServiceRepository serviceRepository;
    private final ServiceTypeDetailRepository typeDetailRepository;
    private final ServiceTypeRepository typeRepository;
    private final ProviderRepository providerRepository;
    private final ModelGroupRepository groupRepository;
    private final VehicleModelRepository modelRepository;
    private final PartRepository partRepository;

    @Override
    public Object findAllByProviderId(Long providerId) {
        return toAllServicesResponses(
                serviceRepository.findAllByProviderId(providerId),
                typeDetailRepository.findAll());
    }

    @Override
    public Object findAllByProviderIdAndTypeId(Long providerId, Long typeId) {
        ServiceType type = typeRepository.findById(typeId)
                .orElseThrow(() -> newExceptionWithId(typeId));
        return toAllServicesResponses(
                serviceRepository.findAllByProviderIdAndTypeDetailType(providerId, type),
                typeDetailRepository.findAllByTypeId(typeId));
    }

    @Override
    public Object findAllByProviderIdAndModelIdAndCategoryIds(Long providerId, Long modelId,
                                                              Set<Long> categoryIds) {
        Map<Long, ServiceOptionResponse> services = new LinkedHashMap<>();

        return services;
    }

    private void validateCreate(Long detailId, Long providerId, Set<Long> modelIds) {
        List<Service> services = serviceRepository.findAllByTypeDetailIdAndProviderId(
                detailId, providerId);

        Set<Long> createdModelIds = services.stream()
                .flatMap(service -> service.getModelGroup().getModels().stream())
                .map(VehicleModel::getId)
                .collect(Collectors.toSet());

        if (CollectionUtils.intersection(modelIds, createdModelIds).size() != 0) {
            throw new InvalidArgumentException("Only one VehicleModel can existed in one ServiceTypeDetail");
        }
    }

    @Override
    public Object create(Long providerId, ServiceInfoRequest request) {
        ServiceTypeDetail typeDetail = typeDetailRepository.findById(request.getTypeDetailId())
                .orElseThrow(() -> newExceptionWithId(request.getTypeDetailId()));
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));

        validateCreate(request.getTypeDetailId(), providerId, request.getGroupPriceRequest().getModelIds());

        Service service = Service.builder()
                .price(request.getGroupPriceRequest().getPrice())
                .typeDetail(typeDetail)
                .provider(provider)
                .modelGroup(groupRepository.save(ModelGroup.builder()
                        .models(new HashSet<>(modelRepository.findAllById(
                                request.getGroupPriceRequest().getModelIds())
                        ))
                        .build()))
                .build();

        return toServiceResponse(serviceRepository.save(service));
    }

    private void validateUpdate(Service updatingService, Set<Long> modelIds) {
        List<Service> services = serviceRepository.findAllByTypeDetailIdAndProviderId(
                updatingService.getTypeDetail().getId(), updatingService.getProvider().getId());
        services.remove(updatingService);

        Set<Long> createdModelIds = services.stream()
                .flatMap(service -> service.getModelGroup().getModels().stream())
                .map(VehicleModel::getId)
                .collect(Collectors.toSet());

        if (CollectionUtils.intersection(modelIds, createdModelIds).size() != 0) {
            throw new InvalidArgumentException("Only one VehicleModel can existed in one ServiceTypeDetail");
        }
    }

    @Override
    public Object update(Long serviceId, GroupPriceRequest request) {
        Service result = serviceRepository.findById(serviceId)
                .orElseThrow(() -> newExceptionWithId(serviceId));

        validateUpdate(result, request.getModelIds());

        result.setPrice(request.getPrice());
        result.getModelGroup().getModels().clear();
        result.getModelGroup().setModels(
                new HashSet<>(modelRepository.findAllById(request.getModelIds())));

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

        Set<Long> modelIds = services.stream()
                .flatMap(service -> service.getModelGroup().getModels().stream())
                .map(VehicleModel::getId)
                .collect(Collectors.toSet());

        if (!modelIds.isEmpty())
            return toModelResponses(modelRepository.findALlByIdNotIn(modelIds));
        else
            return toModelResponses(modelRepository.findAll());
    }
}
