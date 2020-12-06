package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.ModelGroup;
import org.lordrose.vrms.models.responses.GroupResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModelGroupConverter {

    public static GroupResponse toGroupResponse(ModelGroup group) {
        return GroupResponse.builder()
                .build();
    }

    public static List<GroupResponse> toGroupResponses(Collection<ModelGroup> groups) {
        return groups.stream()
                .map(ModelGroupConverter::toGroupResponse)
                .collect(Collectors.toList());
    }
}
