package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {

    private Long userId;
    private String plateNumber;
    private String vinNumber;
    private String color;
    private Long boughtDate;
    private Long modelId;
}
