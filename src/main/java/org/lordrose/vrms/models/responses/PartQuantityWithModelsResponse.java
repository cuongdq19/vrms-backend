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
public class PartQuantityWithModelsResponse {

    private Long id;
    private String name;
    private String description;
    private Double quantity;
    private Double price;
    private Integer warrantyDuration;
    private Integer monthsPerMaintenance;
    private String[] imageUrls;
    private Long sectionId;
    private Long categoryId;
    private String categoryName;
    private Boolean isDeleted;
    private List<VehicleModelResponse> models;
}
