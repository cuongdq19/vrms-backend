package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {

    private String uid;
    private String phoneNumber;
    private String password;
    private String fullName;
    private Boolean gender;
    private String deviceToken;
}
