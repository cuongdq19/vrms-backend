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
public class UserVehicleInfoResponse {

    private Long userId;
    private String phoneNumber;
    private String fullName;
    private Boolean gender;
    private List<VehicleRequestInfoResponse> vehicles;
}
