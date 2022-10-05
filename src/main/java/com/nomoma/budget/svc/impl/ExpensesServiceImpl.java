package com.nomoma.budget.svc.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nomoma.budget.model.dto.ExpensesDto;
import com.nomoma.budget.model.entity.Income;
import com.nomoma.budget.model.entity.Expenses;
import com.nomoma.budget.repo.IncomeRepository;
import com.nomoma.member.repo.MemberRepository;
import com.nomoma.budget.repo.ExpensesRepository;
import com.nomoma.budget.svc.ExpensesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExpensesServiceImpl implements ExpensesService {
    private final ExpensesRepository expensesRepository;
    private final MemberRepository memberRepository;
    private final IncomeRepository incomeRepository;

    @Transactional
    @Override
    public List<ExpensesDto> getExpenses() {
        return expensesRepository.findAllByOrderByDateTimeAsc()
                .stream().map(ExpensesDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<ExpensesDto> getExpensesBy(String quarter) {
        return expensesRepository.getExpensesByQuarter(quarter)
                         .stream().map(ExpensesDto::toDto)
                         .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<ExpensesDto> getExpensesBetween(LocalDateTime begin, LocalDateTime end) {
        return expensesRepository.getExpensesByDateTimeGreaterThanAndDateTimeLessThan(begin, end)
                .stream().map(ExpensesDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ExpensesDto save(ExpensesDto expensesDto) {
        //member 정보 validation check!
//        final List<String> expenseUsers = outcome.getMembers().stream()
//                                                 .filter(name -> memberRepository.findMemberByName(name).isEmpty())
//                                                 .collect(Collectors.toList());
//
//        if (!expenseUsers.isEmpty()) {
//            throw new RuntimeException("member not exists");
//        }

        //잔액 체크
        final int incomeTotal = incomeRepository.getIncomesByQuarter(expensesDto.getQuarter())
                 .stream().map(Income::getAmount)
                 .reduce(Integer::sum).orElse(0);

        final int outcomeTotal = expensesRepository.getExpensesByQuarter(expensesDto.getQuarter())
                .stream().map(Expenses::getAmount)
                .reduce(Integer::sum).orElse(0);

        if (outcomeTotal + expensesDto.getAmount() > incomeTotal) {
            throw new RuntimeException("Oops! You have only " + (incomeTotal - outcomeTotal)
                                       + " won left. Wait for the next quarter!");
        }

        final Expenses saved = expensesRepository.save(ExpensesDto.toEntity(expensesDto, memberRepository));
        return ExpensesDto.toDto(saved);
    }

    @Override
    public void deleteExpensesBy(Long id) {
        expensesRepository.deleteById(id);
    }


}
