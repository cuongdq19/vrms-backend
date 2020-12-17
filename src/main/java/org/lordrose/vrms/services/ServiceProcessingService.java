package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.GroupPriceRequest;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;

import java.util.Set;

public interface ServiceProcessingService {

    Object findAllByProviderId(Long providerId);

    Object findAllByProviderIdAndTypeId(Long providerId, Long typeId);

    Object findAllByProviderIdAndTypeIdModelId(Long providerId, Long typeId, Long modelId);

    Object findAllByProviderIdAndModelIdAndCategoryIds(Long providerId, Long modelId,
                                                       Set<Long> categoryIds);

    Object create(Long providerId, ServiceInfoRequest request);

    Object update(Long serviceId, GroupPriceRequest request);

    void delete(Long serviceId);

    Object findAllModels(Long detailId, Long providerId);
}
