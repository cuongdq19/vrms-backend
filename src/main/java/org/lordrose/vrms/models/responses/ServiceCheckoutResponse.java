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
public class ServiceCheckoutResponse {

    private Long id;
    private Long serviceId;
    private String serviceName;
    private Double servicePrice;
    private String note;
    private List<PartCheckoutResponse> parts;
    private Boolean isIncurred;
}
