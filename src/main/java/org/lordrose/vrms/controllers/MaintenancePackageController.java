package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.constants.MaintenanceConstants;
import org.lordrose.vrms.models.requests.MaintenancePackageRequest;
import org.lordrose.vrms.models.requests.ProviderMaintenanceRequest;
import org.lordrose.vrms.services.MaintenancePackageService;
import org.lordrose.vrms.utils.distances.GeoPoint;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance-packages")
public class MaintenancePackageController {

    private final MaintenancePackageService packageService;
    private final MaintenanceConstants.MaintenanceMilestone milestone;

    @GetMapping("/milestones")
    public Object getMilestones() {
        return milestone.getMilesAsMap();
    }

    @GetMapping("/{packageId}")
    public Object getOneById(@PathVariable Long packageId) {
        return packageService.findById(packageId);
    }

    @GetMapping("/providers/{providerId}")
    public Object getAllByProvider(@PathVariable Long providerId) {
        return packageService.findAllByProviderId(providerId);
    }

    @PostMapping("/providers/{providerId}")
    public Object createPackage(@PathVariable Long providerId,
                                @RequestBody MaintenancePackageRequest request) {
        return packageService.create(providerId, request);
    }

    @PostMapping("/{packageId}")
    public Object updatePackage(@PathVariable Long packageId,
                                @RequestBody MaintenancePackageRequest request) {
        return packageService.update(packageId, request);
    }

    @DeleteMapping("/packages/{packageId}")
    public void deletePackage(@PathVariable Long packageId) {
        packageService.delete(packageId);
    }

    @GetMapping("/providers/{providerId}/models/{modelId}")
    public Object getAllByProviderAndModelId(@PathVariable Long providerId,
                                             @PathVariable Long modelId) {
        return packageService.findAllByProviderIdAndModelId(providerId, modelId);
    }

    @PostMapping("/models/{modelId}")
    public Object getAllBySectionAndModelId(@PathVariable Long modelId,
                                            @RequestBody ProviderMaintenanceRequest request) {
        return packageService.findAllBySectionIdAndModelId(modelId, request);
    }

    @PostMapping("/milestones/{milestoneId}/models/{modelId}")
    public Object getAllByMilestoneAndModelId(@PathVariable Integer milestoneId,
                                              @PathVariable Long modelId,
                                              @RequestBody GeoPoint currentLocation) {
        return packageService.findAllByMilestoneIdAndModelId(milestoneId, modelId, currentLocation);
    }
}
