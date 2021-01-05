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
public class UserRequestInfoResponse {

    private Long id;
    private String phoneNumber;
    private String fullName;
    private Boolean gender;
    private String imageUrl;
    private VehicleResponse vehicle;
}
