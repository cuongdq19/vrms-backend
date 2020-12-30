package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestIncurredUpdateRequest {

    private Map<Long, Map<Long, Map<Long, Double>>> packageMap;
    private Map<Long, Map<Long, Double>> servicePartMap;
    private Set<ExpenseRequest> expenses;
}
