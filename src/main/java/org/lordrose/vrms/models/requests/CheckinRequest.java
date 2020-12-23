package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckinRequest {

    private Set<Long> packageIds;
    private Map<Long, Integer> parts;
    private List<ServiceAndPartsRequest> serviceParts;
    private Set<ExpenseRequest> expenses;
}
