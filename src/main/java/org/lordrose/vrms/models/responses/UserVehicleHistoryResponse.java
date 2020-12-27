package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVehicleHistoryResponse {

    private Long vehicleId;
    private String plateNumber;
    private String vinNumber;
    private String color;
    private Long boughtDate;
    private Long modelId;
    private String modelName;
    private String modelYear;
    private String fuelType;
    private String gearbox;
    private Long userId;
    private String phoneNumber;
    private String fullName;
    private Boolean gender;
    private String userImageUrl;
}
