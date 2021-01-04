package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Contract;
import org.lordrose.vrms.models.responses.ContractResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

public class ContractConverter {

    public static ContractResponse toContractResponse(Contract contract) {
        return ContractResponse.builder()
                .id(contract.getId())
                .fullName(contract.getFullName())
                .address(contract.getAddress())
                .phoneNumber(contract.getPhoneNumber())
                .email(contract.getEmail())
                .contractFileUrls(getUrlsAsArray(contract.getContractFileUrls()))
                .proofImageUrls(getUrlsAsArray(contract.getProofImageUrls()))
                .status(contract.getStatus().textValue)
                .providerId(contract.getProvider() == null ? null : contract.getProvider().getId())
                .build();
    }

    public static List<ContractResponse> toContractResponses(Collection<Contract> contracts) {
        return contracts.stream()
                .map(ContractConverter::toContractResponse)
                .collect(Collectors.toList());
    }
}
