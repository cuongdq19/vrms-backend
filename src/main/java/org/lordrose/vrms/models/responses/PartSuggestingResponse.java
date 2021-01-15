package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartSuggestingResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer warrantyDuration;
    private Integer monthsPerMaintenance;
    private String[] imageUrls;
    private Long sectionId;
    private Long categoryId;
    private String categoryName;
    private Boolean isSupportedByService;
    private List<VehicleModelResponse> models;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartSuggestingResponse that = (PartSuggestingResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
