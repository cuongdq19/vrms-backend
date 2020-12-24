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
public class ProviderDetailResponse {

    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String openTime;
    private String closeTime;
    private Integer slotDuration;
    private Integer slotCapacity;
    private String[] imageUrls;
    private Long manufacturerId;
    private String manufacturerName;
}
