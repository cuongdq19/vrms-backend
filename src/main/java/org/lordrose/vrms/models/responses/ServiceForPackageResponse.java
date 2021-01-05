package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceForPackageResponse {

    private Long id;
    private String name;
    private Double price;
    private Set<PartQuantityResponse> parts;
    private Set<VehicleModelResponse> models;
}
