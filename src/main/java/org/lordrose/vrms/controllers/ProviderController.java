package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.FindProviderWithCategoryRequest;
import org.lordrose.vrms.models.requests.FindProviderWithServicesRequest;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.FeedbackResponse;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.models.responses.ProviderDistanceResponse;
import org.lordrose.vrms.models.responses.SlotResponse;
import org.lordrose.vrms.models.responses.TechnicianResponse;
import org.lordrose.vrms.services.ProviderService;
import org.lordrose.vrms.services.ProviderSuggestingService;
import org.lordrose.vrms.services.RequestSchedulingService;
import org.lordrose.vrms.utils.distances.GeoPoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final RequestSchedulingService schedulingService;
    private final ProviderService providerService;
    private final ProviderSuggestingService suggestingService;

    @GetMapping("/{providerId}/bookings/{secondsOfDate}")
    public List<SlotResponse> getAllAvailableSlots(@PathVariable Long providerId,
                                                   @PathVariable("secondsOfDate") Long date) {
        return schedulingService.getAvailableSlots(providerId, date);
    }

    @GetMapping
    public List<ProviderDetailResponse> getAll() {
        return providerService.findAll();
    }

    @PostMapping
    public List<ProviderDistanceResponse> getAll(@RequestBody GeoPoint currentPos) {
        return providerService.findAll(currentPos);
    }

    @PostMapping("/{id}")
    public ProviderDetailResponse update(@PathVariable Long id,
                                         @RequestBody ProviderRequest request) {
        return providerService.update(id, request);
    }

    @GetMapping("/{providerId}/feedbacks")
    public List<FeedbackResponse> getAllFeedback(@PathVariable Long providerId) {
        return providerService.findAllFeedbackByProviderId(providerId);
    }

    @GetMapping("/{providerId}/timestamp/{time}")
    public List<TechnicianResponse> getAvailableTechnician(@PathVariable Long providerId,
                                                           @PathVariable Long time) {
        return providerService.findAvailableTechnician(providerId, time);
    }

    @PostMapping("/type-details")
    public Object findProvidersByTypeDetails(@RequestBody FindProviderWithServicesRequest request) {
        return suggestingService.findProviders(request);
    }

    @PostMapping("/part-categories")
    public Object findProvidersByPartCategories(@RequestBody FindProviderWithCategoryRequest request) {
        return suggestingService.findProviders(request);
    }
}
