package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.PartCategory;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.ServiceVehiclePart;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.requests.PartRequest;
import org.lordrose.vrms.models.responses.PartProviderResponse;
import org.lordrose.vrms.models.responses.PartResponse;
import org.lordrose.vrms.models.responses.PartSuggestingResponse;
import org.lordrose.vrms.repositories.PartCategoryRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.ServiceVehiclePartRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.repositories.VehiclePartRepository;
import org.lordrose.vrms.services.FeedbackService;
import org.lordrose.vrms.services.PartService;
import org.lordrose.vrms.services.StorageService;
import org.lordrose.vrms.utils.distances.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.PartConverter.toPartResponse;
import static org.lordrose.vrms.converters.PartConverter.toPartResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithIds;
import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;
import static org.lordrose.vrms.utils.distances.DistanceCalculator.calculate;

@RequiredArgsConstructor
@Service
public class PartServiceImpl implements PartService {

    private final VehiclePartRepository partRepository;
    private final VehicleModelRepository modelRepository;
    private final ProviderRepository providerRepository;
    private final PartCategoryRepository categoryRepository;
    private final StorageService storageService;
    private final FeedbackService feedbackService;
    private final ServiceVehiclePartRepository servicePartRepository;

    @Override
    public List<PartResponse> findAllByProviderId(Long providerId) {
        return toPartResponses(partRepository.findAllByProviderIdAndIsDeletedFalse(providerId));
    }

    @Override
    public List<PartResponse> findAllByProviderIdAndModelId(Long providerId, Long modelId) {
        VehicleModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> newExceptionWithId(modelId));
        return toPartResponses(
                partRepository.findAllByProviderIdAndIsDeletedFalseAndModelsContains(
                        providerId, model));
    }

    @Override
    public PartResponse create(PartRequest request, MultipartFile[] images) {
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> newExceptionWithId(request.getProviderId()));
        PartCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> newExceptionWithId(request.getCategoryId()));
        VehiclePart saved = partRepository.save(VehiclePart.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .warrantyDuration(request.getWarrantyDuration())
                .monthsPerMaintenance(request.getMonthsPerMaintenance())
                .imageUrls(storageService.uploadFiles(images))
                .models(new HashSet<>(modelRepository.findAllById(request.getModelIds())))
                .provider(provider)
                .category(category)
                .isDeleted(false)
                .build());
        return toPartResponse(saved);
    }

    @Override
    public PartResponse update(Long partId, PartRequest request) {
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> newExceptionWithId(request.getProviderId()));
        PartCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> newExceptionWithId(request.getCategoryId()));
        VehiclePart result = partRepository.findById(partId)
                .orElseThrow(() -> newExceptionWithId(partId));

        result.setName(request.getName());
        result.setDescription(request.getDescription());
        result.setPrice(request.getPrice());
        result.setImageUrls(result.getImageUrls());
        result.setProvider(provider);
        result.setCategory(category);
        result.setModels(new HashSet<>(modelRepository.findAllById(request.getModelIds())));

        return toPartResponse(partRepository.save(result));
    }

    @Override
    public PartResponse delete(Long partId) {
        VehiclePart result = partRepository.findById(partId)
                .orElseThrow(() -> newExceptionWithId(partId));

       result.setIsDeleted(true);

        return toPartResponse(partRepository.save(result));
    }

    @Override
    public Object findAllByCategoryIdAndProviderId(Long categoryId, Long providerId) {
        List<VehiclePart> parts = partRepository.findAllByCategoryIdAndProviderIdAndIsDeletedFalse(categoryId, providerId);
        return toPartResponses(parts);
    }

    @Transactional
    @Override
    public Object findAllByCategoryIdAndProviderIdAndModelIds(Long categoryId, Long providerId,
                                                              Set<Long> modelIds) {
        Set<VehicleModel> models = new HashSet<>(modelRepository.findAllById(modelIds));
        if (models.size() != modelIds.size()) {
            List<Long> retrievedIds = models.stream()
                    .map(VehicleModel::getId)
                    .collect(Collectors.toList());
            List<Long> notFounds = modelIds.stream()
                    .filter(id -> !retrievedIds.contains(id))
                    .collect(Collectors.toList());
            throw newExceptionWithIds(notFounds);
        }

        List<VehiclePart> parts = partRepository.findAllByProviderIdAndIsDeletedFalseAndCategoryIdAndModelsContains(
                providerId, categoryId, models.stream().findFirst().orElseThrow());

        List<VehiclePart> results = parts.stream()
                .filter(part -> part.getModels().containsAll(models))
                .collect(Collectors.toList());

        return toPartResponses(results);
    }

    @Override
    public Object findByHungCriteria(Long sectionId, Long modelId, GeoPoint currentPos) {
        VehicleModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> newExceptionWithId(modelId));

        List<ServiceVehiclePart> serviceParts = servicePartRepository.findTop10AllByPartCategorySectionIdAndPartModelsContains(
                sectionId, model);

        return serviceParts.stream()
                .map(ServiceVehiclePart::getPart)
                .filter(part -> !part.getIsDeleted())
                .map(part -> PartProviderResponse.builder()
                        .id(part.getProvider().getId())
                        .name(part.getProvider().getName())
                        .address(part.getProvider().getAddress())
                        .imageUrls(getUrlsAsArray(part.getProvider().getImageUrls()))
                        .openTime(part.getProvider().getOpenTime().toString())
                        .closeTime(part.getProvider().getCloseTime().toString())
                        .ratings(feedbackService.getAverageRating(part.getProvider().getId()))
                        .distance(calculate(currentPos, GeoPoint.builder()
                                .latitude(part.getProvider().getLatitude())
                                .longitude(part.getProvider().getLongitude())
                                .build()))
                        .part(PartSuggestingResponse.builder()
                                .id(part.getId())
                                .name(part.getName())
                                .description(part.getDescription())
                                .price(part.getPrice())
                                .warrantyDuration(part.getWarrantyDuration())
                                .monthsPerMaintenance(part.getMonthsPerMaintenance())
                                .imageUrls(getUrlsAsArray(part.getImageUrls()))
                                .sectionId(part.getCategory().getSection().getId())
                                .categoryId(part.getCategory().getId())
                                .categoryName(part.getCategory().getName())
                                .isSupportedByService(true)
                                .models(Collections.emptyList())
                                .build())
                        .build())
                .sorted(Comparator.comparingDouble(PartProviderResponse::getDistance))
                .collect(Collectors.toList());
    }
}
