package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartRequest {

    private Long providerId;
    private String name ;
    private Double price;
    private String description;
    private Set<Long> modelIds;
    private Integer warrantyDuration;
    private Integer monthsPerMaintenance;
}
