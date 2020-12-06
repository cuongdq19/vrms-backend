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
    private String note;
    private String status;
    private String userFullName;
    private String phoneNumber;
    private String plateNumber;
    private Long vehicleTypeId;
    private String vehicleName;
    private Long levelDetailId;
    private Double maintenancePrice;
    private String levelName;
    private Integer levelMonth;
    private Long levelTripDistance;
    private FeedbackResponse feedback;
    private TechnicianResponse technician;
    private List<ServiceCheckoutResponse> services;
    private List<RequestProductResponse> products;
    private List<ExpenseResponse> expenses;
    private ProviderResponse provider;
}
