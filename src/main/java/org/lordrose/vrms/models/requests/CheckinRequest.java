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
public class CheckinRequest {

    private Set<Long> packageIds;
    private Map<Long, ServicePartRequest> serviceParts;
    private Map<Long, Integer> parts;
    private Set<ExpenseRequest> expenses;
}
