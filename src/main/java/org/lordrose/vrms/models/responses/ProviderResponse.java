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
public class ProviderResponse {

    private Long id;
    private String providerName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String openTime;
    private String closeTime;
    private String[] imageUrls;
    private String contractPhoneNumber;
    private String contractEmail;
    private Double ratings;
}
