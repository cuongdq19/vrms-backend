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
public class VehicleModelResponse {

    private Long id;
    private String name;
    private String year;
    private String fuelType;
    private String gearbox;
    private String imageUrl;
    private Long manufacturerId;
    private String manufacturerName;
}
