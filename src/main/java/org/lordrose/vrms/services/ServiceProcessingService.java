package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.GroupPriceRequest;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.models.requests.ServiceNonReplacingInfoRequest;

public interface ServiceProcessingService {

    Object findAllByProviderId(Long providerId);

    Object findAllByProviderIdAndModelId(Long providerId, Long modelId);

    Object findAllByProviderIdAndTypeId(Long providerId, Long typeId);

    Object findAllByProviderIdAndModelIdAndPartIds(Long providerId, Long modelId,
                                                   Long partId);

    Object create(Long providerId, ServiceInfoRequest request);

    Object create(Long providerId, ServiceNonReplacingInfoRequest request);

    Object update(Long serviceId, GroupPriceRequest request);

    Object update(Long serviceId, ServiceNonReplacingInfoRequest request);

    void delete(Long serviceId);

    Object findAllModels(Long detailId, Long providerId);
}
