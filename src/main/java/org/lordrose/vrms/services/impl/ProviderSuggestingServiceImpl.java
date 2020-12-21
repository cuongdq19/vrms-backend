package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.ServiceType;
import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.models.requests.FindProviderWithCategoryRequest;
import org.lordrose.vrms.models.requests.FindProviderWithServicesRequest;
import org.lordrose.vrms.repositories.PartRepository;
import org.lordrose.vrms.repositories.ServiceRepository;
import org.lordrose.vrms.repositories.ServiceTypeRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.FeedbackService;
import org.lordrose.vrms.services.ProviderSuggestingService;

import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ProviderSuggestingServiceImpl implements ProviderSuggestingService {

    private final ServiceRepository serviceRepository;
    private final FeedbackService feedbackService;
    private final VehicleModelRepository modelRepository;
    private final PartRepository partRepository;
    private final ServiceTypeRepository typeRepository;

    @Override
    public Object findProviders(FindProviderWithServicesRequest request) {
        return null;
    }

    @Override
    public Object findProviders(FindProviderWithCategoryRequest request) {
        return null;
    }

    @Override
    public Object findServicesInProvider(Long providerId, Long modelId, Long typeId) {
        VehicleModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> newExceptionWithId(modelId));
        ServiceType type = typeRepository.findById(typeId)
                .orElseThrow(() -> newExceptionWithId(typeId));

        return null;
    }
}
