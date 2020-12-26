package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.PartRequest;
import org.lordrose.vrms.models.responses.PartResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface PartService {

    List<PartResponse> findAllByProviderId(Long providerId);

    List<PartResponse> findAllByProviderIdAndModelId(Long providerId, Long modelId);

    PartResponse create(PartRequest request, MultipartFile[] images);

    PartResponse delete(Long partId);

    PartResponse update(Long partId, PartRequest request);

    Object findAllByCategoryIdAndProviderId(Long categoryId, Long providerId);

    Object findAllByCategoryIdAndProviderIdAndModelIds(Long categoryId, Long providerId,
                                                       Set<Long> modelIds);
}
