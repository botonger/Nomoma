package com.nomoma.budget.svc.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nomoma.budget.model.dto.IncomeDto;
import com.nomoma.budget.model.entity.Income;
import com.nomoma.member.model.dto.MemberDto;
import com.nomoma.member.model.entity.Member;
import com.nomoma.budget.repo.IncomeRepository;
import com.nomoma.member.repo.MemberRepository;
import com.nomoma.budget.svc.IncomeService;
import com.nomoma.util.DateConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
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
    @Scheduled(cron = "0 0 9 1 1/3 ? *")
    public void saveQuarterlyIncome() {
        log.info("===================================");
        log.info("[Scheduler] : [saveQuarterlyIncome]");
        log.info("[Execution Time] : " + DateConverter.currentDateTime());
        log.info("===================================");
        List<Member> members = memberRepository.findAll();
        List<Income> incomes = new ArrayList<>();

        members.forEach(member -> {
            Income income = Income.builder().member(member).quarter(DateConverter.currentQuarter()).amount(200000).build();
            incomes.add(income);
        });

        incomeRepository.saveAll(incomes);
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
