package org.lordrose.vrms.services;

import org.lordrose.vrms.models.requests.FeedbackRequest;
import org.lordrose.vrms.models.requests.RequestIncurredUpdateRequest;
import org.lordrose.vrms.models.requests.RequestInfoRequest;
import org.lordrose.vrms.models.responses.RequestCheckOutResponse;
import org.lordrose.vrms.models.responses.RequestHistoryDetailResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RequestService {

    List<RequestHistoryDetailResponse> findAllByUserId(Long id);

    List<RequestHistoryDetailResponse> findAllByVehicleId(Long vehicleId);

    Object findAllByProviderId(Long providerId);

    RequestCheckOutResponse findRequestById(Long requestId);

    Object create(RequestInfoRequest request);

    RequestCheckOutResponse checkinWithTechnicianId(Long bookingId, Long userId);

    RequestCheckOutResponse update(Long requestId, RequestIncurredUpdateRequest request);

    RequestCheckOutResponse confirm(Long requestId);

    RequestCheckOutResponse finishRepairAndMaintenance(Long requestId);

    RequestCheckOutResponse checkout(Long requestId);

    RequestCheckOutResponse cancel(Long requestId);

    Object sendFeedback(Long requestId, FeedbackRequest request, MultipartFile[] images);
}
