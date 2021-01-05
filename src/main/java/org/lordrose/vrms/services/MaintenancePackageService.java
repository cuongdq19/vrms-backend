package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.MaintenancePackageRequest;
import org.lordrose.vrms.models.requests.ProviderMaintenanceRequest;
import org.lordrose.vrms.utils.distances.GeoPoint;

public interface MaintenancePackageService {

    Object findAllByProviderId(Long providerId);

    Object findById(Long packageId);

    Object create(Long providerId, MaintenancePackageRequest request);

    Object update(Long packageId, MaintenancePackageRequest request);

    void delete(Long packageId);

    Object findAllByProviderIdAndModelId(Long providerId, Long modelId);

    Object findAllBySectionIdAndModelId(Long modelId, ProviderMaintenanceRequest request);

    Object findAllByMilestoneIdAndModelId(Integer milestoneId, Long modelId, GeoPoint currentLocation);

    Object findAllServicesByProviderId(Long providerId);
}
