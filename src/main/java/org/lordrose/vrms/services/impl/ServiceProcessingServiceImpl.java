package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.ModelGroup;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceType;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.models.requests.ServiceRequest;
import org.lordrose.vrms.models.responses.ServiceResponse;
import org.lordrose.vrms.repositories.ModelGroupRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.ServiceProcessingService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceProcessingServiceImpl implements ServiceProcessingService {

    private final ServiceRepository serviceRepository;
    private final ServiceTypeDetailRepository typeDetailRepository;
    private final ProviderRepository providerRepository;
    private final ModelGroupRepository groupRepository;
    private final VehicleModelRepository modelRepository;

    @Override
    public List<ServiceResponse> findAll() {
        return null;
    }

    @Override
    public ServiceResponse create(ServiceRequest request) {
        return null;
    }

    @Override
    public ServiceResponse update(Long id, ServiceRequest request) {
        return null;
    }

    @Override
    public ServiceResponse delete(Long id) {
        return null;
    }

    @Override
    public Object create(Long providerId, ServiceInfoRequest request) {
        ServiceTypeDetail typeDetail = typeDetailRepository.findById(request.getTypeDetailId())
                .orElseThrow(() -> newExceptionWithId(request.getTypeDetailId()));
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));

        List<Service> services = new ArrayList<>();
        request.getGroupPriceRequests()
                .forEach(group -> services.add(Service.builder()
                        .price(group.getPrice())
                        .typeDetail(typeDetail)
                        .provider(provider)
                        .modelGroup(groupRepository.save(ModelGroup.builder()
                                .models(new HashSet<>(modelRepository.findAllById(group.getModelIds())))
                                .build()))
                        .build()));

        return serviceRepository.saveAll(services).stream()
                .map(service -> ServiceResponse.builder()
                        .id(service.getId())
                        .price(service.getPrice())
                        .typeDetail(ServiceTypeDetail.builder()
                                .id(service.getTypeDetail().getId())
                                .name(service.getTypeDetail().getName())
                                .type(ServiceType.builder()
                                        .id(service.getTypeDetail().getType().getId())
                                        .name(service.getTypeDetail().getType().getName())
                                        .build())
                                .partCategory(service.getTypeDetail().getPartCategory())
                                .build())
                        .provider(Provider.builder()
                                .id(service.getProvider().getId())
                                .name(service.getProvider().getName())
                                .address(service.getProvider().getAddress())
                                .build())
                        .modelGroup(ModelGroup.builder()
                                .id(service.getModelGroup().getId())
                                .name(service.getModelGroup().getName())
                                .description(service.getModelGroup().getDescription())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
}
