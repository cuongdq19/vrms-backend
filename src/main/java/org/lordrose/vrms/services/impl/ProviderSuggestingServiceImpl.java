package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.models.requests.FindProviderWithCategoryRequest;
import org.lordrose.vrms.models.requests.FindProviderWithServicesRequest;
import org.lordrose.vrms.models.responses.ProviderSuggestedServiceGroupedResponse;
import org.lordrose.vrms.models.responses.ProviderSuggestedServiceResponse;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.repositories.VehiclePartRepository;
import org.lordrose.vrms.services.FeedbackService;
import org.lordrose.vrms.services.ProviderSuggestingService;
import org.lordrose.vrms.utils.distances.GeoPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.ServiceConverter.toAllServicesResponses;
import static org.lordrose.vrms.converters.ServiceConverter.toServicePriceDetailResponse;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;
import static org.lordrose.vrms.utils.distances.DistanceCalculator.calculate;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ProviderSuggestingServiceImpl implements ProviderSuggestingService {

    private final VehicleModelRepository modelRepository;
    private final ServiceRepository serviceRepository;
    private final FeedbackService feedbackService;
    private final VehiclePartRepository partRepository;
    private final ServiceTypeDetailRepository typeDetailRepository;

    @Override
    public Object findProviders(FindProviderWithServicesRequest request) {
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId()));

        List<Service> services = new ArrayList<>();
        List<ServiceTypeDetail> typeDetails = typeDetailRepository.findAllById(request.getServiceDetailIds());
        request.getServiceDetailIds().forEach(detailId -> services.addAll(
                serviceRepository.findDistinctByTypeDetailIdAndModelGroup_Models_Id(detailId, model.getId())
        ));

        Map<Provider, List<Service>> byProvider = services.stream()
                .filter(service -> {
                    if (!service.getTypeDetail().getType().isReplacingTyped()) {
                        return true;
                    }
                    return partRepository.existsByServices_IdAndModels_Id(service.getId(), model.getId());
                })
                .collect(Collectors.groupingBy(Service::getProvider));

        List<ProviderSuggestedServiceGroupedResponse> responses = new ArrayList<>();
        byProvider.forEach(((provider, serviceList) -> responses.add(
                returnGroupedResponse(provider, request.getCurrentPos(), serviceList, model, typeDetails)
        )));
        return responses.stream()
                .sorted(Comparator.comparingDouble(ProviderSuggestedServiceGroupedResponse::getDistance))
                .collect(Collectors.toList());
    }

    @Override
    public Object findProviders(FindProviderWithCategoryRequest request) {
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId()));
        List<Service> services = new ArrayList<>();
        request.getCategoryIds().forEach(categoryId -> services.addAll(
                serviceRepository.findDistinctByParts_Category_Id(categoryId)));

        Map<Provider, List<Service>> byProvider = services.stream()
                .collect(Collectors.groupingBy(Service::getProvider));

        List<ProviderSuggestedServiceResponse> responses = new ArrayList<>();
        byProvider.forEach(((provider, serviceList) -> responses.add(
                returnResponse(provider, request.getCurrentPos(), serviceList, model))));
        return responses.stream()
                .sorted(Comparator.comparingDouble(ProviderSuggestedServiceResponse::getDistance))
                .collect(Collectors.toList());
    }

    @Override
    public Object findServicesInProvider(Long providerId, Long modelId, Long typeId) {
        return null; //TODO
    }

    private ProviderSuggestedServiceResponse returnResponse(Provider provider, GeoPoint currentPos,
                                                            List<Service> services, VehicleModel model) {
        return ProviderSuggestedServiceResponse.builder()
                .id(provider.getId())
                .name(provider.getName())
                .address(provider.getAddress())
                .imageUrls(getUrlsAsArray(provider.getImageUrls()))
                .openTime(provider.getOpenTime().toString())
                .closeTime(provider.getCloseTime().toString())
                .ratings(feedbackService.getAverageRating(provider.getId()))
                .distance(calculate(currentPos, GeoPoint.builder()
                        .latitude(provider.getLatitude())
                        .longitude(provider.getLongitude())
                        .build()))
                .manufacturerName(provider.getManufacturerName())
                .priceDetails(services.stream()
                        .map(service -> toServicePriceDetailResponse(service,
                                partRepository.findAllByServices_IdAndModelsContains(service.getId(), model)))
                        .collect(Collectors.toList()))
                .build();
    }

    private ProviderSuggestedServiceGroupedResponse returnGroupedResponse(Provider provider, GeoPoint currentPos,
                                                                          List<Service> services, VehicleModel model,
                                                                          List<ServiceTypeDetail> typeDetails) {
        return ProviderSuggestedServiceGroupedResponse.builder()
                .id(provider.getId())
                .name(provider.getName())
                .address(provider.getAddress())
                .imageUrls(getUrlsAsArray(provider.getImageUrls()))
                .openTime(provider.getOpenTime().toString())
                .closeTime(provider.getCloseTime().toString())
                .ratings(feedbackService.getAverageRating(provider.getId()))
                .distance(calculate(currentPos, GeoPoint.builder()
                        .latitude(provider.getLatitude())
                        .longitude(provider.getLongitude())
                        .build()))
                .manufacturerName(provider.getManufacturerName())
                .services(toAllServicesResponses(typeDetails, services.stream()
                        .peek(service -> service.setParts(new HashSet<>(partRepository.findAllByServices_IdAndModelsContains(
                                service.getId(), model))))
                        .collect(Collectors.toList())))
                .build();
    }
}
