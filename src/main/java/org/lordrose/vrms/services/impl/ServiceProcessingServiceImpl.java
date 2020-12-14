package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.ModelGroup;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceType;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.models.requests.GroupPriceRequest;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.repositories.ModelGroupRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.repositories.ServiceTypeRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.ServiceProcessingService;

import java.util.HashSet;

import static org.lordrose.vrms.converters.ServiceConverter.toAllServicesResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServiceResponse;
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
    public Object create(Long providerId, ServiceInfoRequest request) {
        ServiceTypeDetail typeDetail = typeDetailRepository.findById(request.getTypeDetailId())
                .orElseThrow(() -> newExceptionWithId(request.getTypeDetailId()));
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));

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

    @Override
    public Object update(Long serviceId, GroupPriceRequest request) {
        Service result = serviceRepository.findById(serviceId)
                .orElseThrow(() -> newExceptionWithId(serviceId));

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
}
