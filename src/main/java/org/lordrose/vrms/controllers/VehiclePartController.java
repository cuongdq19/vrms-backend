package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.models.requests.PartRequest;
import org.lordrose.vrms.models.responses.PartResponse;
import org.lordrose.vrms.services.PartService;
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
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/parts")
public class VehiclePartController {

    private final PartService partService;

    @PostMapping("/categories/{categoryId}/providers/{providerId}")
    public Object getAllByCategoryAndModelIds(@PathVariable Long categoryId,
                                              @PathVariable Long providerId,
                                              @RequestBody Set<Long> modelIds) {
        return partService.findAllByCategoryIdAndProviderIdAndModelIds(
                categoryId, providerId, modelIds);
    }

    @GetMapping("/categories/{categoryId}/providers/{providerId}")
    public Object getAllByCategory(@PathVariable Long categoryId,
                                   @PathVariable Long providerId) {
        return partService.findAllByCategoryIdAndProviderId(categoryId, providerId);
    }

    @GetMapping("/{providerId}")
    public List<PartResponse> getAllByProviderId(@PathVariable Long providerId) {
        return partService.findAllByProviderId(providerId);
    }
    @GetMapping("/provider/{providerId}/vehicle-model/{modelId}")
    public List<PartResponse> getAllByProviderIdAndModelId(@PathVariable Long providerId,
                                                           @PathVariable Long modelId) {
        return partService.findAllByProviderIdAndModelId(providerId, modelId);
    }

    @PostMapping
    public PartResponse createPart(@ModelAttribute PartRequest request,
                                   @RequestPart MultipartFile[] images) {
        return partService.create(request, images);
    }

    @DeleteMapping("/{partId}")
    public PartResponse deletePart(@PathVariable Long partId) {
        return partService.delete(partId);
    }

    @PostMapping("/{partId}")
    public PartResponse updatePart(@PathVariable Long partId,
                                   @RequestBody PartRequest request) {
        return partService.update(partId, request);
    }
}
