package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderSummaryResponse {

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
    private List<RevenueDetailResponse> revenues;
}
