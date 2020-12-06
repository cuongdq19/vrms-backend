package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.CheckinRequest;
import org.lordrose.vrms.models.requests.FeedbackRequest;
import org.lordrose.vrms.models.requests.RequestInfoRequest;
import org.lordrose.vrms.models.responses.RequestCheckOutResponse;
import org.lordrose.vrms.models.responses.RequestHistoryDetailResponse;

import java.util.List;

public interface RequestService {

    List<RequestHistoryDetailResponse> findAllByUserId(Long id);

    List<RequestHistoryDetailResponse> findAllByVehicleId(Long vehicleId);

    RequestCheckOutResponse findRequestById(Long requestId);

    List<RequestCheckOutResponse> findAllByProviderId(Long providerId);

    RequestCheckOutResponse create(RequestInfoRequest request);

    RequestCheckOutResponse checkinWithTechnicianId(Long bookingId, Long userId);

    RequestCheckOutResponse update(Long requestId, CheckinRequest request);

    RequestCheckOutResponse confirm(Long requestId);

    RequestCheckOutResponse finishRepairAndMaintenance(Long requestId);

    RequestCheckOutResponse checkout(Long requestId);

    RequestCheckOutResponse cancel(Long requestId);

    Object sendFeedback(Long requestId, FeedbackRequest request);
}
