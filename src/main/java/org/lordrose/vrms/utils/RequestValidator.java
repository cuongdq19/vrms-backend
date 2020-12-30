package org.lordrose.vrms.utils;

import org.lordrose.vrms.domains.VehicleModel;
import org.lordrose.vrms.domains.VehiclePart;
import org.lordrose.vrms.exceptions.InvalidArgumentException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lordrose.vrms.exceptions.ResourceNotFoundException.newExceptionWithIds;
import static org.lordrose.vrms.utils.NumberUtils.isInteger;

public class RequestValidator {

    public static void validatePartsRequest(Collection<VehiclePart> parts,
                                            Map<Long, Double> partMap) {
        if (parts.size() != partMap.keySet().size()) {
            List<Long> retrievedIds = parts.stream()
                    .map(VehiclePart::getId)
                    .collect(Collectors.toList());
            List<Long> notFounds = partMap.keySet().stream()
                    .filter(partId -> !retrievedIds.contains(partId))
                    .collect(Collectors.toList());
            throw newExceptionWithIds(notFounds);
        }
        parts.forEach(part -> {
            if (!part.getCategory().getIsSeparable() && !isInteger(partMap.get(part.getId()))) {
                throw new InvalidArgumentException("Invalid part's quantity for Part ID: " + part.getId());
            }
        });
    }

    public static void validateModelsRequest(Collection<VehicleModel> models,
                                             Collection<Long> modelIds) {
        if (models.size() != modelIds.size()) {
            Set<Long> retrievedModelIds = models.stream()
                    .map(VehicleModel::getId)
                    .collect(Collectors.toSet());
            List<Long> missingIds = modelIds.stream()
                    .filter(id -> !retrievedModelIds.contains(id))
                    .collect(Collectors.toList());
            throw newExceptionWithIds(missingIds);
        }
    }
}
