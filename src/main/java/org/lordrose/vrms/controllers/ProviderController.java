package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.FindProviderWithCategoryRequest;
import org.lordrose.vrms.models.requests.FindProviderWithServicesRequest;
import org.lordrose.vrms.models.requests.ProviderRequest;
import org.lordrose.vrms.models.responses.FeedbackResponse;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.models.responses.ProviderDistanceResponse;
import org.lordrose.vrms.models.responses.SlotResponse;
import org.lordrose.vrms.services.ProviderService;
import org.lordrose.vrms.services.ProviderSuggestingService;
import org.lordrose.vrms.services.RequestSchedulingService;
import org.lordrose.vrms.services.impl.ChartServiceImpl;
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
    private final ChartServiceImpl chartService;

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
    public Object getAvailableTechnician(@PathVariable Long providerId,
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

    @GetMapping("/{providerId}/models/{modelId}/types/{typeId}")
    public Object findServicesInProvider(@PathVariable Long providerId,
                                         @PathVariable Long modelId,
                                         @PathVariable Long typeId) {
        return suggestingService.findServicesInProvider(providerId, modelId, typeId);
    }

    @GetMapping("/{providerId}/charts/revenue/{year}")
    public Object getRevenueInfo(@PathVariable Long providerId,
                                 @PathVariable Integer year) {
        return chartService.getRevenueByProvider(providerId, year);
    }

    @GetMapping("/{providerId}/charts/request/{year}")
    public Object getRequestSummary(@PathVariable Long providerId,
                                    @PathVariable Integer year) {
        return chartService.getRequestSummary(providerId, year);
    }

    @GetMapping("/{providerId}/charts/parts/{year}")
    public Object getPartSummary(@PathVariable Long providerId,
                                 @PathVariable Integer year) {
        return chartService.getPartSummary(providerId, year);
    }

    @GetMapping("/ratings/{year}")
    public Object getRatingsSummary(@PathVariable Integer year) {
        return chartService.getRatingsSummary(year);
    }

    @GetMapping("/{providerId}")
    public Object getRatingsByProvider(@PathVariable Long providerId) {
        return providerService.getRatingByProvider(providerId);
    }

    @GetMapping("/{providerId}/rating-summary")
    public Object getFeedbackRatingSummary(@PathVariable Long providerId) {
        return providerService.getRatingSummaryByProvider(providerId);
    }

    @GetMapping("/new")
    public Object getNewestProviders() {
        return chartService.getNewProviderSummary();
    }

    @GetMapping("/customers")
    public Object getNewestCustomers() {
        return chartService.getNewCustomers();
    }

    @GetMapping("/requests/summary")
    public Object getNewestRequests() {
        return chartService.getNewRequests();
    }

    @GetMapping("/requests/summary/ratio")
    public Object getRequestRatioSummary() {
        return chartService.getNewRequestRatios();
    }
}
