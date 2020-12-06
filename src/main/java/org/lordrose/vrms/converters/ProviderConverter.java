package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.models.responses.ProviderResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProviderConverter {

    public static ProviderResponse toProviderResponse(Provider provider) {
        return ProviderResponse.builder().build();
    }

    public static ProviderDetailResponse toProviderDetailResponse(Provider provider) {
        return ProviderDetailResponse.builder()
                .build();
    }

    public static List<ProviderDetailResponse> toProviderDetailResponses(Collection<Provider> providers) {
        return providers.stream()
                .map(ProviderConverter::toProviderDetailResponse)
                .collect(Collectors.toList());
    }
}
