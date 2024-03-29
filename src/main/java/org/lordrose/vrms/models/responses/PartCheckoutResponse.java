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
public class PartCheckoutResponse {

    private Long id;
    private Long partId;
    private String partName;
    private Double quantity;
    private Double price;
    private Integer warrantyDuration;
    private Integer monthsPerMaintenance;
    private String[] imageUrls;
    private Long categoryId;
    private String categoryName;
    private Boolean isDeleted;
}
