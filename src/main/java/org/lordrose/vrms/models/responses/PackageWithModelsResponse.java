package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageWithModelsResponse {

    private Long id;
    private String name;
    private Double milestone;
    private Long sectionId;
    private String sectionName;
    private List<ServiceForPackageResponse> packagedServices;
    private List<VehicleModelResponse> suitableModels;
}
