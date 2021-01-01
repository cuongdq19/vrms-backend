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
public class ProviderDistanceResponse {

    private Long id;
    private String name;
    private String address;
    private String[] imageUrls;
    private String openTime;
    private String closeTime;
    private Double ratings;
    private Double distance;
}
