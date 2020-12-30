package org.lordrose.vrms.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {

    private String name;
    private Double price;
    private String note;
    private Map<Long, Double> parts;
}
