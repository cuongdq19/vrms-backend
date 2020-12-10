package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Contract;
import org.lordrose.vrms.models.responses.ContractResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ContractConverter {

    public static ContractResponse toContractResponse(Contract contract) {
        return ContractResponse.builder().build();
    }

    public static List<ContractResponse> toContractResponses(Collection<Contract> contracts) {
        return contracts.stream()
                .map(ContractConverter::toContractResponse)
                .collect(Collectors.toList());
    }
}
