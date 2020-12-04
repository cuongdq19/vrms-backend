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
public class VehicleResponse {

    private Long id;
    private String plateNumber;
    private String vinNumber;
    private String color;
    private Long boughtDate;
    private Long modelId;
    private String modelName;
    private Long manufacturerId;
    private String manufacturerName;
}
