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
public class ContractResponse {

    private Long id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String[] contractFileUrls;
    private String[] proofImageUrls;
    private String status;
    private Long providerId;
}
