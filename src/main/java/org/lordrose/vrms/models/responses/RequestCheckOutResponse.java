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
public class RequestCheckOutResponse {

    private Long id;
    private Long bookingTime;
    private String note;
    private String status;
    private String[] imageUrls;
    private UserRequestInfoResponse user;
    private List<ServicePackageResponse> packages;
    private List<ServiceCheckoutResponse> services;
}
