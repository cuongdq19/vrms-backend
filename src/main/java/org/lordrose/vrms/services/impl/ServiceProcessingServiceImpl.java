package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceType;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.domains.ServiceVehiclePart;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.exceptions.InvalidArgumentException;
import org.lordrose.vrms.models.requests.GroupPriceRequest;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.models.requests.ServiceNonReplacingInfoRequest;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.repositories.ServiceTypeRepository;
import org.lordrose.vrms.repositories.ServiceVehiclePartRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.repositories.VehiclePartRepository;
import org.lordrose.vrms.services.ServiceProcessingService;
import org.springframework.transaction.annotation.Transactional;

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
import static org.lordrose.vrms.utils.RequestValidator.validateModelsRequest;
import static org.lordrose.vrms.utils.RequestValidator.validatePartsRequest;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceProcessingServiceImpl implements ServiceProcessingService {

    private final ServiceRepository serviceRepository;
    private final ServiceTypeDetailRepository typeDetailRepository;
    private final ServiceTypeRepository typeRepository;
    private final ProviderRepository providerRepository;
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
    public Object findAllByProviderIdAndModelId(Long providerId, Long modelId) {
        Set<Service> services = new LinkedHashSet<>(
                        serviceRepository.findAllByProviderIdAndPartSet_Part_Models_Id(providerId, modelId));
        services = services.stream()
                .filter(service -> !service.getIsDeleted())
                .collect(Collectors.toSet());
        List<Service> list = serviceRepository.findAllByProviderIdAndModels_Id(providerId, modelId).stream()
                .filter(service -> !service.getIsDeleted())
                .collect(Collectors.toList());
        services.addAll(list);
        return toAllServicesResponses(services);
    }

    @Transactional
    @Override
    public Object findAllByProviderIdAndTypeId(Long providerId, Long typeId) {
        ServiceType type = typeRepository.findById(typeId)
                .orElseThrow(() -> newExceptionWithId(typeId));
        return toAllServicesResponses(
                serviceRepository.findAllByProviderIdAndTypeDetailType(providerId, type).stream()
                        .filter(service -> !service.getIsDeleted())
                        .collect(Collectors.toList()));
    }

    @Override
    public Object findAllByProviderIdAndModelIdAndPartIds(Long providerId, Long modelId,
                                                          Long partId) {
        List<Service> services = serviceRepository.findAllByProviderIdAndPartSet_Part_Models_IdAndPartSet_Part_Id(
                providerId, modelId, partId).stream()
                .filter(service -> !service.getIsDeleted())
                .collect(Collectors.toList());

        return toServiceOptionResponses(services);
    }

    @Override
    public Object create(Long providerId, ServiceInfoRequest request) {
        ServiceTypeDetail typeDetail = typeDetailRepository.findById(request.getTypeDetailId())
                .orElseThrow(() -> newExceptionWithId(request.getTypeDetailId()));
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));
        Map<Long, Double> partMap = request.getGroupPriceRequest().getPartQuantity();
        Set<Long> partIds = partMap.keySet();

        List<VehiclePart> parts = partRepository.findAllById(partIds);

        validatePartsRequest(parts, partMap);

        Service service = serviceRepository.save(Service.builder()
                .name(request.getGroupPriceRequest().getName())
                .price(request.getGroupPriceRequest().getPrice())
                .isDeleted(false)
                .typeDetail(typeDetail)
                .provider(provider)
                .build());

        Set<ServiceVehiclePart> list = parts.stream()
                .map(part -> ServiceVehiclePart.builder()
                        .part(part)
                        .quantity(partMap.get(part.getId()))
                        .service(service)
                        .build())
                .collect(Collectors.toSet());
        service.setPartSet(new LinkedHashSet<>(servicePartRepository.saveAll(list)));

        return toServiceResponse(service);
    }

    @Override
    public Object create(Long providerId, ServiceNonReplacingInfoRequest request) {
        ServiceTypeDetail typeDetail = typeDetailRepository.findById(request.getTypeDetailId())
                .orElseThrow(() -> newExceptionWithId(request.getTypeDetailId()));
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));
        Set<Long> modelIds = request.getModelIds();
        Set<VehicleModel> models = new LinkedHashSet<>(modelRepository.findAllById(modelIds));

        validateModelsRequest(models, modelIds);

        Service service = serviceRepository.save(Service.builder()
                .name(request.getServiceName())
                .price(request.getPrice())
                .typeDetail(typeDetail)
                .isDeleted(false)
                .provider(provider)
                .models(models)
                .build());

        return toServiceResponse(service);
    }

    @Transactional
    @Override
    public Object update(Long serviceId, GroupPriceRequest request) {
        Service result = serviceRepository.findById(serviceId)
                .orElseThrow(() -> newExceptionWithId(serviceId));

        if (result.getModels() != null) {
            if (!result.getModels().isEmpty()) {
                throw new InvalidArgumentException("Conflict r bro oi! K lam v duoc dau!");
            }
        }

        Map<Long, Double> partMap = request.getPartQuantity();
        Set<Long> partIds = partMap.keySet();
        List<VehiclePart> parts = partRepository.findAllById(partIds);

        validatePartsRequest(parts, partMap);

        List<ServiceVehiclePart> list = parts.stream()
                .map(part -> ServiceVehiclePart.builder()
                        .part(part)
                        .quantity(partMap.get(part.getId()))
                        .service(result)
                        .build())
                .collect(Collectors.toList());

        result.setName(request.getName());
        result.setPrice(request.getPrice());
        servicePartRepository.deleteAll(result.getPartSet());
        result.getPartSet().clear();
        result.getPartSet().addAll(servicePartRepository.saveAll(list));

        return toServiceResponse(serviceRepository.save(result));
    }

    @Transactional
    @Override
    public Object update(Long serviceId, ServiceNonReplacingInfoRequest request) {
        ServiceTypeDetail typeDetail = typeDetailRepository.findById(request.getTypeDetailId())
                .orElseThrow(() -> newExceptionWithId(request.getTypeDetailId()));
        Service result = serviceRepository.findById(serviceId)
                .orElseThrow(() -> newExceptionWithId(serviceId));
        Set<Long> modelIds = request.getModelIds();

        if (modelIds.isEmpty()) {
            throw new InvalidArgumentException("Conflict r bro oi! K lam v duoc dau!");
        }
        if (result.getModels() == null) {
            throw new InvalidArgumentException("Conflict r bro oi! K lam v duoc dau!");
        } else {
            if (result.getModels().isEmpty()) {
                throw new InvalidArgumentException("Conflict r bro oi! K lam v duoc dau!");
            }
        }

        Set<VehicleModel> models = new LinkedHashSet<>(modelRepository.findAllById(modelIds));

        validateModelsRequest(models, modelIds);

        result.setName(request.getServiceName());
        result.setPrice(request.getPrice());
        result.setTypeDetail(typeDetail);
        result.setModels(models);

        Service service = serviceRepository.save(result);

        return toServiceResponse(service);
    }

    @Override
    public void delete(Long serviceId) {
        serviceRepository.findById(serviceId)
                .ifPresentOrElse(
                        service -> {
                            service.setIsDeleted(true);
                            serviceRepository.save(service);
                        },
                        () -> {throw newExceptionWithId(serviceId);});
    }

    @Override
    public Object findAllModels(Long detailId, Long providerId) {
        return toModelResponses(modelRepository.findAll());
    }
}
