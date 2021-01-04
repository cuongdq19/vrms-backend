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
public class AccessoryResponse {

    private Long id;
    private Double quantity;
    private Integer warrantyDuration;
    private Integer monthsPerMaintenance;
    private PartResponse part;
}
