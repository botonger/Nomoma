package com.nomoma.budget.svc;

import java.time.LocalDateTime;
import java.util.List;

import com.nomoma.budget.model.dto.ExpensesDto;

public interface ExpensesService {
    List<ExpensesDto> getExpenses();
    List<ExpensesDto> getExpensesBy(String quarter);
    List<ExpensesDto> getExpensesBetween(LocalDateTime begin, LocalDateTime end);
    ExpensesDto save(ExpensesDto outcome);
    void deleteExpensesBy(Long id);
}
