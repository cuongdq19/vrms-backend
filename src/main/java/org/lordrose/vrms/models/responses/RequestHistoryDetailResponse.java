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
    private List<PackageHistoryResponse> packages;
    private List<ServiceHistoryResponse> services;
    private List<PartHistoryResponse> parts;
    private List<ExpenseHistoryResponse> expenses;
    private TechnicianResponse technician;
    private UserVehicleHistoryResponse userVehicle;
    private ProviderResponse provider;
    private FeedbackHistoryResponse feedback;
}
