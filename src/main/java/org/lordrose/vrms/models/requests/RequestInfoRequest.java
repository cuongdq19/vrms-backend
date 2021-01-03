package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfoRequest {

    private Long bookingTime;
    private String note;
    private Long vehicleId;
    private Long providerId;
    private List<Long> packageIds;
    private List<Long> serviceIds;
}
