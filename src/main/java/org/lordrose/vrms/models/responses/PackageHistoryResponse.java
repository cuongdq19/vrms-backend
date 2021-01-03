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
public class PackageHistoryResponse {

    private Long packageId;
    private String packageName;
    private Double milestone;
    private Long sectionId;
    private String sectionName;
    private List<ServiceHistoryResponse> services;
}
