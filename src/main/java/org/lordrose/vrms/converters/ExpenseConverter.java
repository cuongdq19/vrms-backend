package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.IncurredExpense;
import org.lordrose.vrms.models.responses.ExpenseResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseConverter {

    public static ExpenseResponse toExpenseResponse(IncurredExpense expense) {
        return ExpenseResponse.builder().build();
    }

    public static List<ExpenseResponse> toExpenseResponses(Collection<IncurredExpense> expenses) {
        return expenses.stream()
                .map(ExpenseConverter::toExpenseResponse)
                .collect(Collectors.toList());
    }
}
