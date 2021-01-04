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
public class PackageProviderResponse {

    private Long id;
    private String name;
    private String address;
    private String[] imageUrls;
    private String openTime;
    private String closeTime;
    private Double ratings;
    private Double distance;
    private List<MaintenancePackageResponse> packages;
}
