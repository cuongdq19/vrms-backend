package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfoRequest {

    private Long bookingTime;
    private String note;
    private Long vehicleId;
    private Long providerId;
    private Set<Long> packageIds;
    private Map<Long, ServicePartRequest> serviceParts;
    private Map<Long, Integer> parts;
}
