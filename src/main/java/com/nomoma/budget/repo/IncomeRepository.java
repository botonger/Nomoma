package com.nomoma.budget.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nomoma.budget.model.entity.Income;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> getIncomesByQuarter(String quarter);
    Optional<Income> getIncomeByMember_IdAndQuarter(Long memberId, String quarter);

    @Override
    Income save(Income income);

    @Modifying
    @Query(value = "UPDATE Income i set i.amount = :amount where i.member.id = :memberId and i.quarter = :quarter")
    int update(@Param("memberId") Long memberId, @Param("amount") int amount, @Param("quarter") String quarter);
}
