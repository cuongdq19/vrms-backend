package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.FeedbackResponse;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.models.responses.ProviderDistanceResponse;
import org.lordrose.vrms.utils.distances.GeoPoint;

import java.util.List;

public interface ProviderService {

    List<ProviderDetailResponse> findAll();

    List<ProviderDistanceResponse> findAll(GeoPoint currentPos);

    ProviderDetailResponse update(Long id, ProviderRequest request);

    List<FeedbackResponse> findAllFeedbackByProviderId(Long providerId);

    Object findAvailableTechnician(Long providerId, Long time);

    Object getRatingByProvider(Long providerId);
}
