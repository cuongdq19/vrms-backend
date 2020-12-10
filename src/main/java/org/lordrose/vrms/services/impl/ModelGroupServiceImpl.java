package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.domains.ModelGroup;
import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.models.requests.GroupRequest;
import org.lordrose.vrms.models.responses.GroupResponse;
import org.lordrose.vrms.repositories.ModelGroupRepository;
import org.lordrose.vrms.repositories.ProviderRepository;
import org.lordrose.vrms.repositories.VehicleModelRepository;
import org.lordrose.vrms.services.ModelGroupService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static org.lordrose.vrms.converters.ModelGroupConverter.toGroupResponse;
import static org.lordrose.vrms.converters.ModelGroupConverter.toGroupResponses;
import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithId;

@RequiredArgsConstructor
@Service
public class ModelGroupServiceImpl implements ModelGroupService {

    private final ModelGroupRepository groupRepository;
    private final VehicleModelRepository modelRepository;
    private final ProviderRepository providerRepository;

    @Override
    public List<GroupResponse> findAllByProvider(Long id) {
        return toGroupResponses(groupRepository.findAllByProviderId(id));
    }

    @Override
    public GroupResponse create(Long providerId, GroupRequest request) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> newExceptionWithId(providerId));
        ModelGroup result = ModelGroup.builder()
                .name(request.getName())
                .description(request.getDescription())
                .provider(provider)
                .models(new HashSet<>())
                .build();

        request.getModelIds().forEach(typeId -> modelRepository.findById(typeId)
                .ifPresent(value -> result.getModels().add(value)));

        return toGroupResponse(groupRepository.save(result));
    }

    @Override
    public GroupResponse updateGroupModels(Long groupId, GroupRequest request) {
        ModelGroup result = groupRepository.findById(groupId)
                .orElseThrow(() -> newExceptionWithId(groupId));

        result.setName(request.getName());
        result.setDescription(request.getDescription());
        result.getModels().clear();
        request.getModelIds().forEach(modelId -> modelRepository.findById(modelId)
                .ifPresent(value -> result.getModels().add(value)));

        return toGroupResponse(groupRepository.save(result));
    }
}
