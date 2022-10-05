package com.nomoma.budget.model.dto;

import com.nomoma.member.model.dto.MemberDto;
import com.nomoma.budget.model.entity.Income;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncomeDto {
    private Long id;
    private String quarter;
    private MemberDto member;
    private int amount;

    public static IncomeDto toDto(Income income) {
        return builder()
                .id(income.getId())
                .quarter(income.getQuarter())
                .member(MemberDto.toDto(income.getMember()))
                .amount(income.getAmount())
                .build();
    }

    public static Income toEntity(IncomeDto incomeDto) {
        return Income.builder()
                     .member(MemberDto.toEntity(incomeDto.getMember()))
                     .quarter(incomeDto.getQuarter())
                     .amount(incomeDto.getAmount())
                     .build();
    }
}
