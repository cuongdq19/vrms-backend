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
public class ServiceAccessoriesResponse {

    private Long serviceRequestId;
    private Long requestId;
    private String serviceName;
    private String serviceNote;
    private Boolean isIncurred;
    private List<AccessoryResponse> accessories;
}
