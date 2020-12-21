package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.GroupPriceRequest;
import org.lordrose.vrms.models.requests.ServiceInfoRequest;
import org.lordrose.vrms.services.ServicePackageProcessingService;
import org.lordrose.vrms.services.ServiceProcessingService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceProcessingService processingService;
    private final ServicePackageProcessingService packageService;

    @GetMapping("/providers/{providerId}")
    public Object findAllServicesByProviderId(@PathVariable Long providerId) {
        return processingService.findAllByProviderId(providerId);
    }

    @GetMapping("/providers/{providerId}/types/{typeId}")
    public Object findAllServicesByProviderIdAndTypeId(@PathVariable Long providerId,
                                                       @PathVariable Long typeId) {
        return processingService.findAllByProviderIdAndTypeId(providerId, typeId);
    }

    @PostMapping("/providers/{providerId}/models/{modelId}")
    public Object findServicesByProviderIdAndModelIdAndPartIds(@PathVariable Long providerId,
                                                         @PathVariable Long modelId,
                                                         @RequestBody Set<Long> partIds) {
        return processingService.findAllByProviderIdAndModelIdAnPartIds(providerId, modelId, partIds);
    }

    @PostMapping("/providers/{providerId}")
    public Object createService(@PathVariable Long providerId,
                                @RequestBody ServiceInfoRequest request) {
        return processingService.create(providerId, request);
    }

    @PostMapping("/{serviceId}")
    public Object updateService(@PathVariable Long serviceId,
                                @RequestBody GroupPriceRequest request) {
        return processingService.update(serviceId, request);
    }

    @DeleteMapping("{serviceId}")
    public void deleteService(@PathVariable Long serviceId) {
        processingService.delete(serviceId);
    }

    @GetMapping("/bases/providers/{providerId}")
    public Object getBases(@PathVariable Long providerId) {
        return packageService.getBases(providerId);
    }

    @GetMapping("/bases/{baseId}/providers/{providerId}")
    public Object getBases(@PathVariable Long baseId, @PathVariable Long providerId) {
        return packageService.getBases(baseId, providerId);
    }

    @PostMapping("/packages/providers/{providerId}")
    public Object createPackage(@PathVariable Long providerId) {
        return packageService.create(providerId);
    }

    @PostMapping("/packages/{packageId}")
    public Object updatePackage(@PathVariable Long packageId) {
        return packageService.update(packageId);
    }

    @DeleteMapping("/packages/{packageId}")
    public void deletePackage(@PathVariable Long packageId) {
        packageService.delete(packageId);
    }

    @GetMapping("type-details/{detailId}/providers/{providerId}")
    public Object getModels(@PathVariable Long detailId,
                            @PathVariable Long providerId) {
        return processingService.findAllModels(detailId, providerId);
    }
}
