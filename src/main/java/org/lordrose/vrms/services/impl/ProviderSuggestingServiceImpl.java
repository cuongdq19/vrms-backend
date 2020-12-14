package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.requests.FindProviderWithCategoryRequest;
import org.lordrose.vrms.models.requests.FindProviderWithServicesRequest;
import org.lordrose.vrms.models.responses.PartResponse;
import org.lordrose.vrms.models.responses.ProviderSuggestedServiceResponse;
import org.lordrose.vrms.models.responses.ServicePriceDetailResponse;
import org.lordrose.vrms.repositories.PartRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.FeedbackService;
import org.lordrose.vrms.services.ProviderSuggestingService;
import org.lordrose.vrms.utils.distances.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;
import static org.lordrose.vrms.utils.distances.DistanceCalculator.calculate;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ProviderSuggestingServiceImpl implements ProviderSuggestingService {

    private final ServiceRepository serviceRepository;
    private final FeedbackService feedbackService;
    private final VehicleModelRepository modelRepository;
    private final PartRepository partRepository;

    @Override
    public Object findProviders(FindProviderWithServicesRequest request) {
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId()));
        List<Service> services = new ArrayList<>();
        request.getServiceDetailIds().forEach(id -> services.addAll(
                serviceRepository.findAllByTypeDetailIdAndModelGroup_Models_Id(id, request.getModelId())
        ));

        Map<Provider, List<Service>> byProvider = services.stream()
                .collect(Collectors.groupingBy(Service::getProvider));

        List<ProviderSuggestedServiceResponse> responses = new ArrayList<>();
        byProvider.forEach((provider, serviceList) -> responses.add(
                ProviderSuggestedServiceResponse.builder()
                        .id(provider.getId())
                        .name(provider.getName())
                        .address(provider.getAddress())
                        .imageUrls(getUrlsAsArray(provider.getImageUrls()))
                        .openTime(provider.getOpenTime().toString())
                        .closeTime(provider.getCloseTime().toString())
                        .ratings(feedbackService.getAverageRating(provider.getId()))
                        .distance(calculate(request.getCurrentPos(), GeoPoint.builder()
                                .latitude(provider.getLatitude())
                                .longitude(provider.getLongitude())
                                .build()))
                        .manufacturerName(provider.getManufacturerName())
                        .totalPrice(serviceList.stream().mapToDouble(Service::getPrice).sum())
                        .priceDetails(serviceList.stream()
                                .map(service -> test(provider.getId(), service, model))
                                .collect(Collectors.toList()))
                        .build()
        ));
        return responses;
    }

    @Override
    public Object findProviders(FindProviderWithCategoryRequest request) {
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId()));
        List<Service> services = new ArrayList<>();
        request.getCategoryIds().forEach(id -> services.addAll(
                serviceRepository.findAllByTypeDetailPartCategory_IdAndModelGroup_Models_Id(id, request.getModelId())
        ));

        Map<Provider, List<Service>> byProvider = services.stream()
                .collect(Collectors.groupingBy(Service::getProvider));

        List<ProviderSuggestedServiceResponse> responses = new ArrayList<>();
        byProvider.forEach((provider, serviceList) -> responses.add(
                ProviderSuggestedServiceResponse.builder()
                        .id(provider.getId())
                        .name(provider.getName())
                        .address(provider.getAddress())
                        .imageUrls(getUrlsAsArray(provider.getImageUrls()))
                        .openTime(provider.getOpenTime().toString())
                        .closeTime(provider.getCloseTime().toString())
                        .ratings(feedbackService.getAverageRating(provider.getId()))
                        .distance(calculate(request.getCurrentPos(), GeoPoint.builder()
                                .latitude(provider.getLatitude())
                                .longitude(provider.getLongitude())
                                .build()))
                        .manufacturerName(provider.getManufacturerName())
                        .totalPrice(serviceList.stream().mapToDouble(Service::getPrice).sum())
                        .priceDetails(serviceList.stream()
                                .map(service -> test(provider.getId(), service, model))
                                .collect(Collectors.toList()))
                        .build()
        ));
        return responses;
    }

    private ServicePriceDetailResponse test(Long providerId, Service service, VehicleModel model) {
        Long categoryId = service.getTypeDetail().getPartCategoryId();
        List<VehiclePart> parts = partRepository.findAllByProviderIdAndCategoryIdAndModelsContains(
                providerId, categoryId, model);
        return ServicePriceDetailResponse.builder()
                .serviceId(service.getId())
                .serviceName(service.getTypeDetail().getType().getName() + " - " +
                        service.getTypeDetail().getSection().getName() +
                        service.getTypeDetail().getPartCategoryName())
                .servicePrice(service.getPrice())
                .parts(parts.stream()
                        .map(part -> PartResponse.builder()
                                .id(part.getId())
                                .name(part.getName())
                                .price(part.getPrice())
                                .imageUrls(getUrlsAsArray(part.getImageUrls()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
