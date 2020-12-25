package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceNonReplacingInfoRequest {

    private String serviceName;
    private Double price;
    private Long typeDetailId;
    private Set<Long> modelIds;
}
