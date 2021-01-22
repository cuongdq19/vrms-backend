package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.constants.SuggestingValueConfig;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.Service;
import org.lordrose.vrms.domains.ServiceTypeDetail;
import org.lordrose.vrms.domains.ServiceVehiclePart;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.models.requests.FindProviderWithCategoryRequest;
import org.lordrose.vrms.models.requests.FindProviderWithServicesRequest;
import org.lordrose.vrms.models.responses.PartSuggestingResponse;
import org.lordrose.vrms.models.responses.ProviderSuggestedPartResponse;
import org.lordrose.vrms.models.responses.ProviderSuggestedServiceGroupedResponse;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceTypeDetailRepository;
import org.lordrose.vrms.repositories.ServiceVehiclePartRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.FeedbackService;
import org.lordrose.vrms.services.ProviderSuggestingService;
import org.lordrose.vrms.utils.distances.GeoPoint;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.PartConverter.toPartSuggestingResponse;
import static org.lordrose.vrms.converters.ServiceConverter.toAllServicesResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;
import static org.lordrose.vrms.utils.distances.DistanceCalculator.calculate;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ProviderSuggestingServiceImpl implements ProviderSuggestingService {

    private final SuggestingValueConfig suggestingValue;

    private final VehicleModelRepository modelRepository;
    private final ServiceRepository serviceRepository;
    private final FeedbackService feedbackService;
    private final ServiceTypeDetailRepository typeDetailRepository;
    private final ServiceVehiclePartRepository servicePartRepository;

    @Transactional
    @Override
    public Object findProviders(FindProviderWithServicesRequest request) {
        final Long modelId = request.getModelId();

        Set<Service> services = new LinkedHashSet<>();
        List<ServiceTypeDetail> typeDetails = typeDetailRepository.findAllById(request.getServiceDetailIds());
        request.getServiceDetailIds().forEach(detailId -> {
            services.addAll(
                    serviceRepository.findAllByTypeDetailIdAndPartSet_Part_Models_Id(detailId, modelId).stream()
                            .filter(service -> service.getPartSet().stream()
                                    .noneMatch(part -> part.getPart().getIsDeleted()))
                            .collect(Collectors.toList()));
            services.addAll(
                    serviceRepository.findAllByTypeDetailIdAndModels_Id(detailId, modelId));});

        Map<Provider, List<Service>> byProvider = services.stream()
                .collect(Collectors.groupingBy(Service::getProvider));

        List<ProviderSuggestedServiceGroupedResponse> responses = new ArrayList<>();
        byProvider.forEach(((provider, serviceList) -> responses.add(
                returnGroupedResponse(provider, request.getCurrentPos(), serviceList, typeDetails)
        )));
        return responses.stream()
                .sorted(Comparator.comparingDouble(ProviderSuggestedServiceGroupedResponse::getDistance))
                .filter(provider -> provider.getDistance() <= suggestingValue.getDistanceLimit())
                .collect(Collectors.toList());
    }

    @Override
    public Object findProviders(FindProviderWithCategoryRequest request) {
        String sortBy = request.getSortBy();
        VehicleModel model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> newExceptionWithId(request.getModelId()));
        List<ServiceVehiclePart> parts = new ArrayList<>();
        request.getCategoryIds().forEach(categoryId -> parts.addAll(
                servicePartRepository.findAllByPartCategoryIdAndPartIsDeletedAndPartModelsContains(
                        categoryId, false, model)));

        Map<Provider, List<ServiceVehiclePart>> byProvider = parts.stream()
                .collect(Collectors.groupingBy(part -> part.getPart().getProvider()));

        List<ProviderSuggestedPartResponse> responses = new ArrayList<>();
        byProvider.forEach(((provider, partList) -> responses.add(
                returnResponse(provider, request.getCurrentPos(), partList))));
        return sortBy(responses.stream()
                .filter(provider -> provider.getDistance() <= suggestingValue.getDistanceLimit())
                .collect(Collectors.toList()), sortBy);
    }

    @Override
    public Object findServicesInProvider(Long providerId, Long modelId, Long typeId) {
        return null; //TODO
    }

    private ProviderSuggestedPartResponse returnResponse(Provider provider, GeoPoint currentPos,
                                                         List<ServiceVehiclePart> partList) {
        return ProviderSuggestedPartResponse.builder()
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
                .suggestedParts(new ArrayList<>(partList.stream()
                        .map(part -> toPartSuggestingResponse(part.getPart()))
                        .collect(Collectors.toSet())))
                .build();
    }

    private ProviderSuggestedServiceGroupedResponse returnGroupedResponse(Provider provider, GeoPoint currentPos,
                                                                          List<Service> services,
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
                .services(toAllServicesResponses(typeDetails, services))
                .build();
    }

    private List<ProviderSuggestedPartResponse> sortBy(List<ProviderSuggestedPartResponse> responses, String value) {
        String upperCasedValue = String.valueOf(value).toUpperCase();
        switch (upperCasedValue) {
            case "RATING":
                return responses.stream()
                        .sorted(Comparator.comparingDouble(ProviderSuggestedPartResponse::getRatings).reversed())
                        .collect(Collectors.toList());
            case "DISTANCE":
                return responses.stream()
                        .sorted(Comparator.comparingDouble(ProviderSuggestedPartResponse::getDistance))
                        .collect(Collectors.toList());
            default:
                return responses.stream()
                        .sorted(Comparator.comparingDouble(response -> response.getSuggestedParts().stream()
                                .min(Comparator.comparingDouble(PartSuggestingResponse::getPrice))
                                .orElseThrow().getPrice()))
                        .collect(Collectors.toList());
        }
    }
}
