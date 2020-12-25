package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.GroupPriceRequest;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;

public interface ServiceProcessingService {

    Object findAllByProviderId(Long providerId);

    Object findAllByProviderIdAndTypeId(Long providerId, Long typeId);

    Object findAllByProviderIdAndModelIdAndPartIds(Long providerId, Long modelId,
                                                   Long partId);

    Object create(Long providerId, ServiceInfoRequest request);

    Object update(Long serviceId, GroupPriceRequest request);

    void delete(Long serviceId);

    Object findAllModels(Long detailId, Long providerId);
}
