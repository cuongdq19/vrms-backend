package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Provider;
import org.lordrose.vrms.models.responses.ProviderDetailResponse;
import org.lordrose.vrms.models.responses.ProviderResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.utils.FileUrlUtils.getUrlsAsArray;

public class ProviderConverter {

    public static ProviderResponse toProviderResponse(Provider provider) {
        return ProviderResponse.builder()
                .id(provider.getId())
                .build();
    }

    public static ProviderDetailResponse toProviderDetailResponse(Provider provider) {
        return ProviderDetailResponse.builder()
                .id(provider.getId())
                .name(provider.getName())
                .address(provider.getAddress())
                .latitude(provider.getLatitude())
                .longitude(provider.getLongitude())
                .openTime(provider.getOpenTime().toString())
                .closeTime(provider.getCloseTime().toString())
                .slotDuration(provider.getSlotDuration())
                .slotCapacity(provider.getSlotCapacity())
                .imageUrls(getUrlsAsArray(provider.getImageUrls()))
                .manufacturerId(provider.getManufacturerId())
                .manufacturerName(provider.getManufacturerName())
                .build();
    }

    public static List<ProviderDetailResponse> toProviderDetailResponses(Collection<Provider> providers) {
        return providers.stream()
                .map(ProviderConverter::toProviderDetailResponse)
                .collect(Collectors.toList());
    }

    public static ProviderResponse toProviderHistoryResponse(Provider provider) {
        return ProviderResponse.builder()
                .id(provider.getId())
                .providerName(provider.getName())
                .address(provider.getAddress())
                .latitude(provider.getLatitude())
                .longitude(provider.getLongitude())
                .openTime(provider.getOpenTime().toString())
                .closeTime(provider.getCloseTime().toString())
                .imageUrls(getUrlsAsArray(provider.getImageUrls()))
                .contractPhoneNumber(provider.getContract().getPhoneNumber())
                .contractEmail(provider.getContract().getEmail())
                .build();
    }
}
