package com.nomoma.budget.svc;

import java.util.List;

import com.nomoma.budget.model.dto.IncomeDto;

public interface IncomeService {
    List<IncomeDto> getIncomes();
    List<IncomeDto> getIncomesBy(String quarter);
    IncomeDto saveIncome(IncomeDto income);
    IncomeDto updateIncome(IncomeDto income);
}
