package com.nomoma.budget.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nomoma.budget.model.entity.Expenses;

public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    List<Expenses> findAllByOrderByDateTimeAsc();

    List<Expenses> getExpensesByQuarter(String quarter);
    List<Expenses> getExpensesByDateTimeGreaterThanAndDateTimeLessThan(LocalDateTime begin, LocalDateTime end);

    @Override
    Expenses save(Expenses expenses);

    @Override
    void deleteById(Long id);
}
