package com.nomoma.budget.svc.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nomoma.budget.model.dto.IncomeDto;
import com.nomoma.budget.model.entity.Income;
import com.nomoma.member.model.entity.Member;
import com.nomoma.budget.repo.IncomeRepository;
import com.nomoma.member.repo.MemberRepository;
import com.nomoma.budget.svc.IncomeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<IncomeDto> getIncomes() {
        final List<Income> incomes = incomeRepository.findAll();
        return incomes.stream().map(IncomeDto::toDto).collect(Collectors.toList());
    }

    //분기별 팀비 배정 목록
    @Transactional
    @Override
    public List<IncomeDto> getIncomesBy(String quarter) {
        final List<Income> incomes = incomeRepository.getIncomesByQuarter(quarter);
        return incomes.stream().map(IncomeDto::toDto).collect(Collectors.toList());
    }

    //팀비 저장
    @Transactional
    @Override
    public IncomeDto saveIncome(IncomeDto incomeDto) {
        final Member member = memberRepository.findById(incomeDto.getMember().getId())
                                              .orElseThrow(() -> new NullPointerException("No such member exists"));

        final Income saved = incomeRepository.save(IncomeDto.toEntity(incomeDto));
        return IncomeDto.toDto(saved);
    }

    //팀비 수정
    @Transactional
    @Override
    public IncomeDto updateIncome(IncomeDto incomeDto) {
        final Income income = incomeRepository.getIncomeByMember_IdAndQuarter(incomeDto.getMember().getId(), incomeDto.getQuarter())
                                                    .orElseThrow(() -> new NullPointerException("No income exists"));
        income.changeAmount(incomeDto.getAmount());
        return IncomeDto.toDto(income);
    }
}
