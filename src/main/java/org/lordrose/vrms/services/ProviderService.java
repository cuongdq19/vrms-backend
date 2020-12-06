package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.FeedbackResponse;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.models.responses.TechnicianResponse;

import java.util.List;

public interface ProviderService {

    List<ProviderDetailResponse> findAll();

    ProviderDetailResponse update(Long id, ProviderRequest request);

    List<FeedbackResponse> findAllFeedbackByProviderId(Long providerId);

    List<TechnicianResponse> findAvailableTechnician(Long providerId, Long time);
}
