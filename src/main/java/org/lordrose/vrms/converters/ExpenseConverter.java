package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.IncurredExpense;
import org.lordrose.vrms.models.responses.ExpenseResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseConverter {

    public static ExpenseResponse toExpenseResponse(IncurredExpense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .name(expense.getName())
                .price(expense.getPrice())
                .description(expense.getDescription())
                .build();
    }

    public static List<ExpenseResponse> toExpenseResponses(Collection<IncurredExpense> expenses) {
        if (expenses == null) {
            expenses = Collections.emptyList();
        }
        return expenses.stream()
                .map(ExpenseConverter::toExpenseResponse)
                .collect(Collectors.toList());
    }
}
