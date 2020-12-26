package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.constants.MaintenanceConstants;
import org.lordrose.vrms.models.requests.ServicePackageRequest;
import org.lordrose.vrms.services.ServicePackageProcessingService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service-packages")
public class ServicePackageController {

    private final ServicePackageProcessingService packageService;
    private final MaintenanceConstants.MaintenanceMilestone milestone;

    @GetMapping("/milestones")
    public Object getMilestones() {
        return milestone.getMilesAsMap();
    }

    @PostMapping("/packages/providers/{providerId}")
    public Object createPackage(@PathVariable Long providerId,
                                @RequestBody ServicePackageRequest request) {
        return packageService.create(providerId, request);
    }

    @PostMapping("/packages/{packageId}")
    public Object updatePackage(@PathVariable Long packageId,
                                @RequestBody ServicePackageRequest request) {
        return packageService.update(packageId, request);
    }

    @DeleteMapping("/packages/{packageId}")
    public void deletePackage(@PathVariable Long packageId) {
        packageService.delete(packageId);
    }
}
