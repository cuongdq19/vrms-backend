package org.lordrose.vrms.converters;

import org.lordrose.vrms.domains.IncurredExpense;
import org.lordrose.vrms.models.responses.ExpenseHistoryResponse;
import org.lordrose.vrms.models.responses.ExpenseResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.lordrose.vrms.converters.PartConverter.toPartDetailResponses;
import static org.lordrose.vrms.converters.PartConverter.toPartHistoryResponses;

public class ExpenseConverter {

    public static ExpenseResponse toExpenseResponse(IncurredExpense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .name(expense.getName())
                .price(expense.getPrice())
                .description(expense.getDescription())
                .parts(toPartDetailResponses(expense.getParts()))
                .build();
    }

    public static List<ExpenseResponse> toExpenseResponses(Collection<IncurredExpense> expenses) {
        if (expenses == null)
            expenses = Collections.emptyList();
        return expenses.stream()
                .map(ExpenseConverter::toExpenseResponse)
                .collect(Collectors.toList());
    }

    public static ExpenseHistoryResponse toExpenseHistoryResponse(IncurredExpense expense) {
        return ExpenseHistoryResponse.builder()
                .id(expense.getId())
                .name(expense.getName())
                .price(expense.getPrice())
                .description(expense.getDescription())
                .parts(toPartHistoryResponses(expense.getParts()))
                .build();
    }

    public static List<ExpenseHistoryResponse> toExpenseHistoryResponses(Collection<IncurredExpense> expenses) {
        if (expenses == null)
            expenses = Collections.emptyList();
        return expenses.stream()
                .map(ExpenseConverter::toExpenseHistoryResponse)
                .collect(Collectors.toList());
    }
}
