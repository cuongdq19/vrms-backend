package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.Accessory;
import org.lordrose.vrms.models.responses.AccessoryResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.PartConverter.toEmptyModelPartResponse;

public class AccessoryConverter {

    public static AccessoryResponse toAccessoryResponse(Accessory accessory) {
        return AccessoryResponse.builder()
                .id(accessory.getId())
                .quantity(accessory.getQuantity())
                .part(toEmptyModelPartResponse(accessory.getPart()))
                .build();
    }

    public static List<AccessoryResponse> toAccessoryResponses(Collection<Accessory> accessories) {
        return accessories.stream()
                .map(AccessoryConverter::toAccessoryResponse)
                .collect(Collectors.toList());
    }
}
