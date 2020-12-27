package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartHistoryResponse {

    private Long partId;
    private Double quantity;
    private Double priceEach;
    private String partName;
    private String description;
    private String[] partImageUrls;
    private Integer warrantyDuration;
    private Long categoryId;
    private String categoryName;
    private Long sectionId;
    private String sectionName;
    private Boolean isAccessory;
}
