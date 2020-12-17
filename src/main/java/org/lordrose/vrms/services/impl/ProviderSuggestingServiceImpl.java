package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.models.requests.FindProviderWithCategoryRequest;
import org.lordrose.vrms.models.requests.FindProviderWithServicesRequest;
import org.lordrose.vrms.models.responses.ProviderSuggestedServiceResponse;
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

import static org.lordrose.vrms.converters.ServiceConverter.toServicePriceDetailResponse;
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
        byProvider.forEach(
                (provider, serviceList) -> responses.add(
                        test(provider, request.getCurrentPos(), serviceList, model))
        );
        return responses;
    }

    @Override
    public Object findProviders(FindProviderWithCategoryRequest request) {
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId()));
        List<Service> temps = new ArrayList<>();
        request.getCategoryIds().forEach(id -> temps.addAll(
                serviceRepository.findAllByTypeDetailPartCategory_IdAndModelGroup_Models_Id(id, request.getModelId())
        ));

        List<Service> services = temps.stream()
                .filter(service -> partRepository.existsByCategoryIdAndProviderId(
                        service.getTypeDetail().getPartCategoryId(), service.getProvider().getId()))
                .collect(Collectors.toList());

        Map<Provider, List<Service>> byProvider = services.stream()
                .collect(Collectors.groupingBy(Service::getProvider));

        List<ProviderSuggestedServiceResponse> responses = new ArrayList<>();
        byProvider.forEach((provider, serviceList) -> {
            if (!serviceList.isEmpty())
                responses.add(test(provider, request.getCurrentPos(), serviceList, model));});
        return responses;
    }

    @Override
    public Object findServiceInProvider(Long providerId, Long modelId) {
        VehicleModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> newExceptionWithId(modelId));
        List<Service> services = new ArrayList<>(
                serviceRepository.findAllByProviderIdAndModelGroup_Models_Id(providerId, modelId)
        );

        return services.stream()
                .map(service -> toServicePriceDetailResponse(service,
                        partRepository.findAllByProviderIdAndCategoryIdAndModelsContains(
                                providerId, service.getTypeDetail().getPartCategoryId(), model
                        )))
                .collect(Collectors.toList());
    }

    private ProviderSuggestedServiceResponse test(Provider provider, GeoPoint currentPos,
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
                .totalPrice(services.stream().mapToDouble(Service::getPrice).sum())
                .priceDetails(services.stream()
                        .map(service -> toServicePriceDetailResponse(service,
                                partRepository.findAllByProviderIdAndCategoryIdAndModelsContains(
                                        provider.getId(), service.getTypeDetail().getPartCategoryId(), model
                                )))
                        .collect(Collectors.toList()))
                .build();
    }
}
