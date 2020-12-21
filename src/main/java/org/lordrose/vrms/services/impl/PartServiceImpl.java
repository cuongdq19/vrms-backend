package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.PartCategory;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.models.requests.PartRequest;
import org.lordrose.vrms.models.responses.PartResponse;
import org.lordrose.vrms.repositories.PartCategoryRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.repositories.VehiclePartRepository;
import org.lordrose.vrms.services.PartService;
import org.lordrose.vrms.services.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

import static org.lordrose.vrms.converters.PartConverter.toPartResponse;
import static org.lordrose.vrms.converters.PartConverter.toPartResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;

@RequiredArgsConstructor
@Service
public class PartServiceImpl implements PartService {

    private final VehiclePartRepository partRepository;
    private final VehicleModelRepository modelRepository;
    private final ProviderRepository providerRepository;
    private final PartCategoryRepository categoryRepository;
    private final StorageService storageService;

    @Override
    public List<PartResponse> findAllByProviderId(Long providerId) {
        return toPartResponses(partRepository.findAllByProviderId(providerId));
    }

    @Override
    public List<PartResponse> findAllByProviderIdAndModelId(Long providerId, Long modelId) {
        VehicleModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> newExceptionWithId(modelId));
        return toPartResponses(
                partRepository.findAllByProviderIdAndModelsContains(providerId, model)
        );
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

        partRepository.deleteById(partId);

        return toPartResponse(result);
    }
}
