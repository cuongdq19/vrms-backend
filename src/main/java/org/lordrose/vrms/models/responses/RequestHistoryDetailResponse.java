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
public class RequestHistoryDetailResponse {

    private Long id;
    private Long bookingTime;
    private Long arriveTime;
    private Long checkoutTime;
    private String note;
    private String status;
    private List<PackageCheckoutResponse> packages;
    private List<ServiceCheckoutResponse> services;
    private TechnicianResponse technician;
    private UserVehicleHistoryResponse userVehicle;
    private ProviderResponse provider;
    private FeedbackHistoryResponse feedback;
}
