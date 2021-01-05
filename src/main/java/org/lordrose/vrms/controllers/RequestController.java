package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.FeedbackRequest;
import org.lordrose.vrms.models.requests.RequestIncurredUpdateRequest;
import org.lordrose.vrms.models.requests.RequestInfoRequest;
import org.lordrose.vrms.models.responses.RequestCheckOutResponse;
import org.lordrose.vrms.models.responses.RequestHistoryDetailResponse;
import org.lordrose.vrms.services.RequestService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/users/{id}")
    public List<RequestHistoryDetailResponse> getAllBookingByUserId(@PathVariable Long id) {
        return requestService.findAllByUserId(id);
    }

    @GetMapping("vehicles/{vehicleId}")
    public List<RequestHistoryDetailResponse> getAllBookingByVehicleId(@PathVariable Long vehicleId) {
        return requestService.findAllByVehicleId(vehicleId);
    }

    @GetMapping("/{requestId}")
    public RequestCheckOutResponse getBookingRequestById(@PathVariable Long requestId) {
        return requestService.findRequestById(requestId);
    }

    @GetMapping("/providers/{providerId}")
    public Object getAllBookingByProviderId(@PathVariable Long providerId) {
        return requestService.findAllByProviderId(providerId);
    }

    @PostMapping
    public Object create(@RequestBody RequestInfoRequest request) {
        return requestService.create(request);
    }

    @PostMapping("/checkin/{bookingId}/technicians/{userId}")
    public RequestCheckOutResponse checkinWithTechnician(@PathVariable Long bookingId,
                                                  @PathVariable Long userId) {
        return requestService.checkinWithTechnicianId(bookingId, userId);
    }

    @PostMapping("/update/{requestId}")
    public RequestCheckOutResponse updateBookingRequest(@PathVariable Long requestId,
                                                                @RequestBody RequestIncurredUpdateRequest request) {
        return requestService.update(requestId, request);
    }

    @GetMapping("/confirm/{requestId}")
    public RequestCheckOutResponse confirmBookingRequest(@PathVariable Long requestId) {
        return requestService.confirm(requestId);
    }

    @GetMapping("/done/{requestId}")
    public RequestCheckOutResponse finishedRepairAndMaintenance(@PathVariable Long requestId) {
        return requestService.finishRepairAndMaintenance(requestId);
    }

    @GetMapping("/checkout/{requestId}")
    public RequestCheckOutResponse checkoutBookingRequest(@PathVariable Long requestId) {
        return requestService.checkout(requestId);
    }

    @DeleteMapping("/{requestId}")
    public RequestCheckOutResponse cancelBookingRequest(@PathVariable Long requestId) {
        return requestService.cancel(requestId);
    }

    @PostMapping("{requestId}/feedbacks")
    public Object sendFeedback(@PathVariable Long requestId,
                               @RequestPart(required = false) MultipartFile[] images,
                               @ModelAttribute FeedbackRequest request) {
        if (images == null) {
            images = new MultipartFile[0];
        }
        return requestService.sendFeedback(requestId, request, images);
    }
}
