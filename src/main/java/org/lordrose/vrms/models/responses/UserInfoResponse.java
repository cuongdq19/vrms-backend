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
public class UserInfoResponse {

    private Long id;
    private String fullName;
    private Boolean gender;
    private String[] imgUrl;
    private Long roleId;
    private String roleName;
    private Boolean isActive;
    private Long providerId;
}
