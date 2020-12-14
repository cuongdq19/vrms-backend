package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.ModelGroup;
import org.lordrose.vrms.models.responses.GroupResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.VehicleModelConverter.toModelResponses;

public class ModelGroupConverter {

    public static GroupResponse toGroupResponse(ModelGroup group) {
        return GroupResponse.builder()
                //.id(group.getId()) remove due to redundancy
                .models(toModelResponses(group.getModels()))
                .build();
    }

    public static List<GroupResponse> toGroupResponses(Collection<ModelGroup> groups) {
        return groups.stream()
                .map(ModelGroupConverter::toGroupResponse)
                .collect(Collectors.toList());
    }
}
