package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRequest {

    private String providerName;
    private String address;
    private Double latitude;
    private Double longitude;
    private Long openTime;
    private Long closeTime;
    private Integer slotDuration;
    private Integer slotCapacity;
    private Long manufacturerId;
}
