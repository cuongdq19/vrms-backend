package org.lordrose.vrms.controllers;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.services.impl.ImageDetectionServiceImpl;
import org.lordrose.vrms.utils.distances.GeoPoint;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/detections")
public class ImageDetectionController {

    private final ImageDetectionServiceImpl detectionService;

    @PostMapping("/parts/categories")
    public Object getCategory(@RequestParam("file") MultipartFile image) {
        return detectionService.getCategoryId(image);
    }

    @PostMapping("/models/{modelId}/parts")
    public Object getPartsByImage(@PathVariable Long modelId,
                                  @RequestParam("latitude") Double latitude,
                                  @RequestParam("longitude") Double longitude,
                                  @RequestParam("file") MultipartFile image) {
        GeoPoint currentPos = GeoPoint.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
        return detectionService.findProvidersWithParts(modelId, currentPos, image);
    }
}
